package ee.taltech.dbcsql.core.model.dsl.restriction;

import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;

public interface RestrictionVisitor <T>
{
	public T visit(ConnectionRestriction r);
	public T visit(ExpressionRestriction r);
}
