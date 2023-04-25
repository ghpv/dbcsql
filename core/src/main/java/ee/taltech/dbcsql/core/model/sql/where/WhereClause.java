package ee.taltech.dbcsql.core.model.sql.where;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNode;

public class WhereClause
{
	private WhereNode node;

	public WhereClause()
	{
	}

	public WhereClause(WhereNode node)
	{
		this.setNode(node);
	}

	public WhereNode getNode()
	{
		return node;
	}

	public void setNode(WhereNode node)
	{
		this.node = node;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof WhereClause))
		{
			return super.equals(obj);
		}
		WhereClause other = (WhereClause) obj;
		return true
			&& this.node.equals(other.node)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.node
		);
	}

	@Override
	public String toString()
	{
		return node.toString();
	}

	public void combineWith(WhereNode node)
	{
		WhereNodeCompositor compositor = new WhereNodeCompositor();
		if (this.node == null)
		{
			this.node = node;
			return;
		}
		this.node.accept(compositor);
		WhereNode n = node.accept(compositor);
		this.setNode(n);

		if (n instanceof CompositeWhereNode)
		{
			CompositeWhereNode nc = (CompositeWhereNode) n;
			if (nc.getNodes().size() == 1)
			{
				this.setNode(nc.getNodes().get(0));
			}
		}
	}
}
