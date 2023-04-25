package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;

public class UnaryExpressionNode implements ExpressionNode
{
	private ExpressionNode node;
	private boolean negate = false;

	public UnaryExpressionNode()
	{
	}

	public UnaryExpressionNode(ExpressionNode node, boolean negate)
	{
		this.setNegate(negate);
		this.setNode(node);
	}

	public UnaryExpressionNode clone()
	{
		return new UnaryExpressionNode(this.node.clone(), this.negate);
	}

	public void setNode(ExpressionNode node)
	{
		this.node = node;
	}

	public boolean isNegate()
	{
		return negate;
	}

	public void setNegate(boolean negate)
	{
		this.negate = negate;
	}

	public ExpressionNode getNode()
	{
		return node;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof UnaryExpressionNode))
		{
			return super.equals(obj);
		}
		UnaryExpressionNode other = (UnaryExpressionNode) obj;
		return true
			&& this.node.equals(other.node)
			&& this.negate == other.negate
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.node,
			this.negate
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.negate ? "!" : "")
			.append(this.node)
			.toString()
		;
	}

	@Override
	public <T> T accept(RestrictionExpressionNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
