package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node;

public interface ExpressionNode
{
	public <T> T accept(RestrictionExpressionNodeVisitor<T> visitor);
	public ExpressionNode clone();
}
