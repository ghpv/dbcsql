package ee.taltech.dbcsql.core.model.dsl.restriction.expression;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNode;

public class ExpressionNodeCompositor implements RestrictionExpressionNodeVisitor<CompositeExpressionNode>
{
	private CompositeExpressionNode target = new CompositeExpressionNode();
	private Set<ExpressionNode> seenNodes = new HashSet<>();

	public void setOperation(BooleanOperation op)
	{
		this.target.setOperation(op);
	}

	@Override
	public CompositeExpressionNode visit(CompositeExpressionNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}

		if (node.getOperation() == this.target.getOperation())
		{
			for (ExpressionNode n: node.getNodes())
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
	public CompositeExpressionNode visit(UnaryExpressionNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}

		this.target.addNode(node);
		return this.target;
	}

	@Override
	public CompositeExpressionNode visit(ComparisonExpressionNode node)
	{
		if (!seenNodes.add(node))
		{
			return this.target;
		}
		this.target.addNode(node);
		return this.target;
	}
}
