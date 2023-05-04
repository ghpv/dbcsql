package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;

public class ComparisonTargetNodeStringifierVisitor implements ComparisonTargetNodeVisitor<String>
{
	@Override
	public String visit(CompositeComparisonTargetNode v)
	{
		List<ComparisonTargetNode> subNodes = v.getNodes();

		if (subNodes.size() == 1)
		{
			return subNodes.get(0).accept(this);
		}

		return this.joinSubnodes(
			v.getNodes(),
			v.getOperation().getSymbol()
		);
	}

	@Override
	public String visit(LiteralComparisonTargetNode v)
	{
		return v.getSymbol();
	}

	@Override
	public String visit(FunctionComparisonTargetNode v)
	{
		return v.getFunction() + this.joinSubnodes(v.getParameters(), ",");
	}

	private String joinSubnodes(Collection<ComparisonTargetNode> nodes, String delimiter)
	{
		List<String> subVals = new LinkedList<>();
		for (ComparisonTargetNode n: nodes)
		{
			subVals.add(n.accept(this));
		}
		return "(" + String.join(delimiter, subVals) + ")";
	}
}
