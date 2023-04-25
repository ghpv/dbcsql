package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ConnectedColumns;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.VariableLink;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionNodeRestrictionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestrictionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class TargettedPostcondtionToRestrictionVisitor
	extends ConditionToRestrictionTranslator
	implements PostconditionVisitor<Void>
{
	private VariableDef currentVariable;

	public TargettedPostcondtionToRestrictionVisitor(GenerationContext ctx)
	{
		super(ctx);
	}

	@Override
	public Void visit(DeletedPostcondition c)
	{
		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		this.handleLinks(
			c.getTarget(),
			c.getLinks()
		);
		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		this.handleLinks(
			c.getTarget(),
			c.getLinks()
		);
		return null;
	}

	private void handleLinks(VariableDef target, Collection<VariableLink> links)
	{
		this.currentVariable = target;
		boolean newValue = true;
		for (VariableLink linked: links)
		{
			this.linkVariables(
				target,
				linked.getKey(),
				linked.getValue(),
				newValue,
				linked.isUnlink()
			);
		}
	}

	@Override
	protected void addConnectionKeyColumnLinkToComposite(
		ExpressionRestrictionBuilderBase<ExpressionNodeRestrictionBuilder>.CompositeExpressionNodeSubBuilder builder,
		VariableDef fkeyA,
		VariableDef fkeyB,
		ConnectedColumns conn
	) {
		assert this.currentVariable != null;

		if (
			shouldProcessLinkWith(conn.getA())
			&& shouldProcessLinkWith(conn.getB())
		) {
			super.addConnectionKeyColumnLinkToComposite(builder, fkeyA, fkeyB, conn);
		}
	}

	private boolean shouldProcessLinkWith(ColumnDef col)
	{
		return !col
			.getTable()
			.getAliasedName()
			.equals(this
				.currentVariable
				.getTable()
				.getAliasedName()
			)
		;
	}
}
