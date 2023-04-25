package ee.taltech.dbcsql.core.model.sql.where.unary;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.WhereNodeVisitor;

public class UnaryWhereNode implements WhereNode
{
	private WhereNode node;
	private boolean negate = false;

	public UnaryWhereNode()
	{
	}

	public UnaryWhereNode(WhereNode node, boolean negate)
	{
		this.setNegate(true);
		this.setNode(node);
	}

	public void setNode(WhereNode node)
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

	public WhereNode getNode()
	{
		return node;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof UnaryWhereNode))
		{
			return super.equals(obj);
		}
		UnaryWhereNode other = (UnaryWhereNode) obj;
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
	public <T> T accept(WhereNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
