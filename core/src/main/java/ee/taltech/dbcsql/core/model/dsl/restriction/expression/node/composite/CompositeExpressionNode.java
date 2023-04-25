package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.dsl.generic.CompositeNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;

public class CompositeExpressionNode
	extends CompositeNode<ExpressionNode, BooleanOperation>
	implements ExpressionNode
{
	public CompositeExpressionNode()
	{
		super(BooleanOperation.AND);
	}

	public CompositeExpressionNode(List<ExpressionNode> nodes, BooleanOperation mode)
	{
		this();
		this.setNodes(nodes);
		this.setOperation(mode);
	}

	public CompositeExpressionNode clone()
	{
		CompositeExpressionNode copy = new CompositeExpressionNode();
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
	public <T> T accept(RestrictionExpressionNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
