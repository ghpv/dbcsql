package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNode;

public interface RestrictionExpressionNodeVisitor <T>
{
	public T visit(CompositeExpressionNode node);
	public T visit(UnaryExpressionNode node);
	public T visit(ComparisonExpressionNode node);
}
