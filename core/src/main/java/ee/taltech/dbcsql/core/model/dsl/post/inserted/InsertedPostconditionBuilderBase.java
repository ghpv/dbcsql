package ee.taltech.dbcsql.core.model.dsl.post.inserted;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class InsertedPostconditionBuilderBase<BuilderT extends InsertedPostconditionBuilderBase<BuilderT>>
{
	protected InsertedPostcondition data = new InsertedPostcondition();

	@SuppressWarnings("unchecked")
	public BuilderT withTarget(VariableDef target)
	{
		this.data.setTarget(target);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addValue(String dslName, ComparisonTargetNode value)
	{
		this.data.addValue(dslName, value);
		return (BuilderT) this;
	}

	public BuilderT linkWith(VariableDef var)
	{
		return this.linkWith(var, List.of());
	}

	@SuppressWarnings("unchecked")
	public BuilderT linkWith(VariableDef var, List<String> names)
	{
		this.data.linkWith(var, names);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withReturnValue(ReturnValue val)
	{
		this.data.setReturnValue(val);
		return (BuilderT) this;
	}
}
