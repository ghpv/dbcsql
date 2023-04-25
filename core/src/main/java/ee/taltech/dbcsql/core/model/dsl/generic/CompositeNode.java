package ee.taltech.dbcsql.core.model.dsl.generic;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CompositeNode<NodeType, CompositionEnum>
{
	private List<NodeType> nodes = new LinkedList<>();
	private CompositionEnum operation;

	public CompositeNode(CompositionEnum defaultOperation)
	{
		this.operation = defaultOperation;
	}

	public List<NodeType> getNodes()
	{
		return nodes;
	}
	public void addNode(NodeType node)
	{
		this.nodes.add(node);
	}
	public void extendWith(CompositeNode<NodeType, CompositionEnum> cb)
	{
		this.extendWith(cb.getNodes());
	}
	public void extendWith(Collection<NodeType> nodes)
	{
		nodes
			.stream()
			.forEach(x -> this.addNode(x))
		;
	}
	public void setNodes(List<NodeType> nodes)
	{
		this.nodes = nodes;
	}
	public CompositionEnum getOperation()
	{
		return operation;
	}
	public void setOperation(CompositionEnum mode)
	{
		this.operation = mode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof CompositeNode))
		{
			return super.equals(obj);
		}
		CompositeNode<NodeType, CompositionEnum> other = (CompositeNode<NodeType, CompositionEnum>) obj;
		return true
			&& this.operation.equals(other.operation)
			&& this.nodes.equals(other.nodes)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.operation,
			this.nodes.hashCode()
		);
	}

	@Override
	public String toString()
	{
		return "(" + String.join(" " + this.operation + " ", this
			.nodes
			.stream()
			.map(x -> x.toString())
			.collect(Collectors.toList())
		) + ")";
	}
}
