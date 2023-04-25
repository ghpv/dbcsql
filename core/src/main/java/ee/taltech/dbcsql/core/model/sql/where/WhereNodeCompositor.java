package ee.taltech.dbcsql.core.model.sql.where;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNode;

public class WhereNodeCompositor implements WhereNodeVisitor<CompositeWhereNode>
{
	private CompositeWhereNode target = new CompositeWhereNode();
	private Set<WhereNode> seenNodes = new HashSet<>();

	public void setOperation(BooleanOperation op)
	{
		this.target.setOperation(op);
	}

	@Override
	public CompositeWhereNode visit(CompositeWhereNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}

		if (node.getOperation() == this.target.getOperation())
		{
			for (WhereNode n: node.getNodes())
			{
				n.accept(this);
			}
		}
		else
		{
			this.target.addNode(node);
		}
		return this.target;
	}

	@Override
	public CompositeWhereNode visit(UnaryWhereNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}

		this.target.addNode(node);
		return this.target;
	}

	@Override
	public CompositeWhereNode visit(ComparisonWhereNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}
		this.target.addNode(node);
		return this.target;
	}
}
