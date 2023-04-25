package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.db.ConnectedColumns;
import ee.taltech.dbcsql.core.model.db.DSLName;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.VariableLink;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionNodeRestrictionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestrictionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestrictionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class PostcondtionToRestrictionVisitor
	extends ConditionToRestrictionTranslator
	implements PostconditionVisitor<Void>
{
	private VariableDef currentTarget;
	private Map<String, Runnable> workload = new HashMap<>();

	public PostcondtionToRestrictionVisitor(GenerationContext ctx)
	{
		super(ctx);
	}

	public void apply()
	{
		for (Runnable r: workload.values())
		{
			r.run();
		}
	}

	@Override
	public Void visit(DeletedPostcondition c)
	{
		this.reg.addIgnoredVariable(c.getTarget());
		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		this.registerValuesAsRestrictions(
			c.getTarget(),
			c.getValues()
		);
		this.handleLinks(
			c.getTarget(),
			c.getLinks()
		);
		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		this.registerValuesAsRestrictions(
			c.getTarget(),
			c.getValues()
		);
		this.handleLinks(
			c.getTarget(),
			c.getLinks()
		);
		return null;
	}

	private void registerValuesAsRestrictions(VariableDef target, Collection<ColumnEquality> values)
	{
		for (ColumnEquality eq: values)
		{
			DSLName n = eq.getKey().getAliasedName().getDSLName();

			String workIdentifier = new StringBuilder()
				.append("val:")
				.append(target.getAlias())
				.append(":")
				.append(n.getName())
				.toString()
			;
			this.workload.put(workIdentifier, () ->
			{
				boolean useIs = this.requiresIs(eq.getValue());
				this.addComparison(
					target,
					eq.getKey().getAliasedName().getDSLName(),
					useIs ? ComparisonType.IS : ComparisonType.EQUALS,
					eq.getValue()
				);
			});
		}
	}

	private void handleLinks(VariableDef target, Collection<VariableLink> links)
	{
		boolean newValue = false;
		for (VariableLink linked: links)
		{
			String workIdentifier = new StringBuilder()
				.append("link:")
				.append(target.getAlias())
				.append(":")
				.append(linked.getKey().getAlias())
				.toString()
			;
			this.workload.put(workIdentifier, () ->
			{
				if (linked.isUnlink())
				{
					this.currentTarget = target;
				}
				else
				{
					this.currentTarget = null;
				}
				this.linkVariables(
					target,
					linked.getKey(),
					linked.getValue(),
					newValue,
					linked.isUnlink()
				);
			});
		}
	}

	@Override
	protected void addConnectionKeyColumnLinkToComposite(
		ExpressionRestrictionBuilderBase<ExpressionNodeRestrictionBuilder>.CompositeExpressionNodeSubBuilder builder,
		VariableDef fkeyA,
		VariableDef fkeyB,
		ConnectedColumns conn
	) {
		if (this.currentTarget != null) // Don't add connection at all if we are unlinked
		{
			if (!this.keysRelevant(fkeyA, fkeyB))
			{
				return;
			}

			ColumnDef ctc = conn.getOneBelongingTo(this.currentTarget.getTable());
			this.addComparison(
				this.currentTarget,
				ctc.getAliasedName().getDSLName(),
				ComparisonType.IS,
				new LiteralComparisonTargetNode("null")
			);
		}
		else
		{
			super.addConnectionKeyColumnLinkToComposite(builder, fkeyA, fkeyB, conn);
		}
	}

	private boolean keysRelevant(VariableDef fkeyA, VariableDef fkeyB)
	{
		return this.keyRelevant(fkeyA)
			|| this.keyRelevant(fkeyB)
		;
	}

	private boolean keyRelevant(VariableDef fk)
	{
		return this.currentTarget.getAlias().equals(fk.getAlias());
	}

	private void addComparison(
		VariableDef target,
		DSLName targetColumn,
		ComparisonType comparison,
		ComparisonTargetNode value
	) {
		Restriction r = new ExpressionRestrictionBuilder()
			.makeComparison()
				.withColumn(target, targetColumn.getName())
				.withType(comparison)
				.withTargets(value)
			.build()
		.build()
		;
		this.reg.extendRestriction(r);
	}

	private boolean requiresIs(ComparisonTargetNode value)
	{
		if (!(value instanceof LiteralComparisonTargetNode))
		{
			return false;
		}
		LiteralComparisonTargetNode literal = (LiteralComparisonTargetNode) value;
		return literal.getSymbol().toLowerCase().equals("null");
	}
}
