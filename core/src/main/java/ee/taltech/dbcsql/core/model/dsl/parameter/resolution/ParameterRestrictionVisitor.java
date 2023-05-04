package ee.taltech.dbcsql.core.model.dsl.parameter.resolution;

import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;

public class ParameterRestrictionVisitor implements RestrictionVisitor<Void>
{
	private ParameterResolver resolver;

	public ParameterRestrictionVisitor(ParameterResolver resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public Void visit(ConnectionRestriction r)
	{
		return null;
	}

	@Override
	public Void visit(ExpressionRestriction r)
	{
		r.getNode().accept(new ParameterRestrictionExpressionVisitor(this.resolver));
		return null;
	}
}
