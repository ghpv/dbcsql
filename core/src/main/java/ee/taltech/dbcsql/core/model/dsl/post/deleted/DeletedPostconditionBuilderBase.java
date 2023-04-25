package ee.taltech.dbcsql.core.model.dsl.post.deleted;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class DeletedPostconditionBuilderBase<BuilderT extends DeletedPostconditionBuilderBase<BuilderT>>
{
	protected DeletedPostcondition data = new DeletedPostcondition();

	@SuppressWarnings("unchecked")
	public BuilderT withTarget(VariableDef target)
	{
		this.data.setTarget(target);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withReturnValue(ReturnValue val)
	{
		this.data.setReturnValue(val);
		return (BuilderT) this;
	}
}
