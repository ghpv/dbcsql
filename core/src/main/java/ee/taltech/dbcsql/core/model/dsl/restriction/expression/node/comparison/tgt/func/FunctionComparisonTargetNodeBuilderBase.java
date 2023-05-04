package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;

public class FunctionComparisonTargetNodeBuilderBase<BuilderT extends FunctionComparisonTargetNodeBuilderBase<BuilderT>>
{
	protected FunctionComparisonTargetNode data = new FunctionComparisonTargetNode();

	@SuppressWarnings("unchecked")
	public BuilderT withFunction(String function)
	{
		this.data.setFunction(function);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addParameter(ComparisonTargetNode param)
	{
		this.data.addParameter(param);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withParameters(List<ComparisonTargetNode> parameters)
	{
		this.data.setParameters(parameters);
		return (BuilderT) this;
	}
}
