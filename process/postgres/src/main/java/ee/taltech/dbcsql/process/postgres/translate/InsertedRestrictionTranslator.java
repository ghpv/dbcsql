package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatementBuilder;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatementBuilderBase;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class InsertedRestrictionTranslator extends RestrictionTranslator implements RestrictionVisitor<Void>
{
	private InsertStatementBuilderBase<InsertStatementBuilder>.WhereClauseBuilder where;
	private InsertStatementBuilderBase<InsertStatementBuilder>.RelatedTablesBuilder relatedTables;
	private InsertStatementBuilder isb;

	public InsertedRestrictionTranslator(GenerationContext ctx, VariableDef target, InsertStatementBuilder isb)
	{
		super(ctx, target);
		this.isb = isb;
		this.where = isb.where();
		this.relatedTables = isb.relatedTables();
	}

	@Override
	public Void visit(ConnectionRestriction r)
	{
		this.usedTablesInConnection(r,
			var ->
			{
				relatedTables.add(CommonTranslation.translateVariableForDeclaration(var));
			}
		);
		this.handleLinkWith(
			r,
			(targetColumn, otherColumn) -> isb.addValue(targetColumn, otherColumn)
		);
		return null;
	}

	@Override
	public Void visit(ExpressionRestriction r)
	{
		this.handleExpression(
			r,
			x -> relatedTables.add(x),
			x -> relatedTables.remove(x),
			x -> where.extendWith(x)
		);
		return null;
	}

	public void done()
	{
		if (this.where != null) where.build();
		if (this.relatedTables != null) relatedTables.build();
	}
}
