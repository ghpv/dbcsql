package ee.taltech.dbcsql.process.postgres.translate;

import java.util.List;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ConnectedColumns;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestrictionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionNodeRestrictionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestrictionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.output.RestrictionRegistry;

public class ConditionToRestrictionTranslator
{
	protected RestrictionRegistry reg;
	protected DatabaseDef db;

	public ConditionToRestrictionTranslator(GenerationContext ctx)
	{
		this.db = ctx.getDatabaseDef();
		this.reg = ctx.getRestrictionRegistry();
	}

	protected void linkVariables(VariableDef a, VariableDef b, List<String> names, boolean newValue, boolean unlink)
	{
		this
			.addConnectionKeyColumnLinks(a, b, names)
			.addConnectionVariableRestrictions(a, b, names, newValue, unlink)
		;
	}

	protected boolean isIgnored(VariableDef a)
	{
		return reg.isIgnored(a);
	}

	protected ConditionToRestrictionTranslator addConnectionVariableRestrictions(
		VariableDef a,
		VariableDef b,
		List<String> names,
		boolean newValue,
		boolean unlink

	) {
		Restriction r = new ConnectionRestrictionBuilder()
			.betweenVariables(a, b)
			.withNames(names)
			.withNewValue(newValue)
			.withUnlink(unlink)
			.build()
		;
		reg.extendRestriction(r);
		return this;
	}

	protected ConditionToRestrictionTranslator addConnectionKeyColumnLinks(VariableDef a, VariableDef b, List<String> names)
	{
		ExpressionNodeRestrictionBuilder tableConnections = new ExpressionNodeRestrictionBuilder();
		var composite = tableConnections.makeNode();
		for (FKey fk: this.db.getConnection(a.getTable(), b.getTable(), names))
		{
			for (ConnectedColumns conn: fk.getConnections())
			{
				addConnectionKeyColumnLinkToComposite(composite, a, b, conn);
			}
		}
		composite.build();
		ExpressionRestriction connections = tableConnections.build();
		reg.extendRestriction(connections);
		return this;
	}

	protected void addConnectionKeyColumnLinkToComposite(
		ExpressionRestrictionBuilderBase<ExpressionNodeRestrictionBuilder>.CompositeExpressionNodeSubBuilder builder,
		VariableDef fkeyA,
		VariableDef fkeyB,
		ConnectedColumns conn
	) {
		VariableDef ownerA = getRelevantFromConnection(fkeyA, fkeyB, conn.getA());
		VariableDef ownerB = getRelevantFromConnection(fkeyA, fkeyB, conn.getB());

		builder
			.extendWithComparison()
				.withColumn(ownerA, conn.getA().getAliasedName())
				.withType(ComparisonType.EQUALS)
				.withLiteralTargets(CommonTranslation.translateColumnName(ownerB, conn.getB()))
			.build()
		;
	}

	private VariableDef getRelevantFromConnection(VariableDef a, VariableDef b, ColumnDef col)
	{
		if (a.getTable().hasColumn(col))
		{
			return a;
		}
		if (b.getTable().hasColumn(col))
		{
			return b;
		}
		return new VariableDef(
			col.getTable().getAliasedName().getDBName().getName(),
			col.getTable()
		);
	}
}
