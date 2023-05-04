package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;

public class FunctionComparisonTargetNode implements ComparisonTargetNode
{
	private String function;
	private List<ComparisonTargetNode> parameters = new LinkedList<>();

	public FunctionComparisonTargetNode()
	{
	}

	public FunctionComparisonTargetNode(String symbol)
	{
		this.setFunction(symbol);
	}

	public FunctionComparisonTargetNode clone()
	{
		return new FunctionComparisonTargetNode(this.function);
	}

	public void setFunction(String symbol)
	{
		this.function = symbol;
	}

	public String getFunction()
	{
		return this.function;
	}

	public List<ComparisonTargetNode> getParameters()
	{
		return parameters;
	}

	public void addParameter(ComparisonTargetNode param)
	{
		this.parameters.add(param);
	}

	public void setParameters(List<ComparisonTargetNode> parameters)
	{
		this.parameters = parameters;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof FunctionComparisonTargetNode))
		{
			return super.equals(obj);
		}
		FunctionComparisonTargetNode other = (FunctionComparisonTargetNode) obj;
		return true
			&& this.function.equals(other.function)
			&& this.parameters.equals(other.parameters)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.function,
			this.parameters.hashCode()
		);
	}

	@Override
	public String toString()
	{
		return this.function;
	}

	@Override
	public <T> T accept(ComparisonTargetNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
