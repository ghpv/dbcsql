package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.dsl.generic.CompositeNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetOperation;

public class CompositeComparisonTargetNode
	extends CompositeNode<ComparisonTargetNode, ComparisonTargetOperation>
	implements ComparisonTargetNode
{
	public CompositeComparisonTargetNode()
	{
		super(ComparisonTargetOperation.PLUS);
	}

	public CompositeComparisonTargetNode(List<ComparisonTargetNode> nodes, ComparisonTargetOperation mode)
	{
		this();
		this.setNodes(nodes);
		this.setOperation(mode);
	}

	public CompositeComparisonTargetNode clone()
	{
		CompositeComparisonTargetNode copy = new CompositeComparisonTargetNode();
		copy.setOperation(this.getOperation());
		copy.setNodes(this
			.getNodes()
			.stream()
			.map(x -> x.clone())
			.collect(Collectors.toCollection(LinkedList::new))
		);
		return copy;
	}

	@Override
	public <T> T accept(ComparisonTargetNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
