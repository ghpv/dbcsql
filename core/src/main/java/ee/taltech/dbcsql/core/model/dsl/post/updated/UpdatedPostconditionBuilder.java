package ee.taltech.dbcsql.core.model.dsl.post.updated;

public class UpdatedPostconditionBuilder extends UpdatedPostconditionBuilderBase<UpdatedPostconditionBuilder>
{
	public UpdatedPostcondition build()
	{
		return this.data;
	}
}
