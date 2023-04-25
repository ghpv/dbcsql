package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;

public class ComparisonTargetNodeCompositor implements ComparisonTargetNodeVisitor<CompositeComparisonTargetNode>
{
	private CompositeComparisonTargetNode target = new CompositeComparisonTargetNode();

	public void setOperation(ComparisonTargetOperation op)
	{
		this.target.setOperation(op);
	}

	@Override
	public CompositeComparisonTargetNode visit(CompositeComparisonTargetNode node)
	{
		if (node.getOperation() == this.target.getOperation())
		{
			for (ComparisonTargetNode n: node.getNodes())
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
	public CompositeComparisonTargetNode visit(LiteralComparisonTargetNode node)
	{
		this.target.addNode(node);
		return this.target;
	}

	@Override
	public CompositeComparisonTargetNode visit(FunctionComparisonTargetNode node)
	{
		this.target.addNode(node);
		return null;
	}
}
