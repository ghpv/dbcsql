package ee.taltech.dbcsql.core.model.sql.where;

import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNode;

public interface WhereNodeVisitor <T>
{
	public T visit(ComparisonWhereNode node);
	public T visit(UnaryWhereNode node);
	public T visit(CompositeWhereNode node);
}
