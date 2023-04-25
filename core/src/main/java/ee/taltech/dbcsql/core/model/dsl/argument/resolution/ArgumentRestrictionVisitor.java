package ee.taltech.dbcsql.core.model.dsl.argument.resolution;

import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;

public class ArgumentRestrictionVisitor implements RestrictionVisitor<Void>
{
	private ArgumentResolver resolver;

	public ArgumentRestrictionVisitor(ArgumentResolver resolver)
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
		r.getNode().accept(new ArgumentRestrictionExpressionVisitor(this.resolver));
		return null;
	}
}
