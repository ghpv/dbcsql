package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatementBuilder;
import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatementBuilderBase;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class DeletedRestrictionTranslator extends RestrictionTranslator implements RestrictionVisitor<Void>
{
	private DeleteStatementBuilderBase<DeleteStatementBuilder>.UsingClauseBuilder using;
	private DeleteStatementBuilderBase<DeleteStatementBuilder>.WhereClauseBuilder where;

	public DeletedRestrictionTranslator(GenerationContext ctx, VariableDef target, DeleteStatementBuilder dsb)
	{
		super(ctx, target);
		this.using = dsb.using();
		this.where = dsb.where();
	}

	@Override
	public Void visit(ConnectionRestriction r)
	{
		this.usedTablesInConnection(r,
			var -> using.add(CommonTranslation.translateVariableForDeclaration(var))
		);
		return null;
	}

	@Override
	public Void visit(ExpressionRestriction r)
	{
		this.handleExpression(
			r,
			x -> using.add(x),
			x -> using.remove(x),
			x -> where.extendWith(x)
		);
		return null;
	}

	public void done()
	{
		if (where != null) where.build();
		if (using != null) using.build();
	}
}
