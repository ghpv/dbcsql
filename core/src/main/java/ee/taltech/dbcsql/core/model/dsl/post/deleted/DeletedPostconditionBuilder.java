package ee.taltech.dbcsql.core.model.dsl.post.deleted;

public class DeletedPostconditionBuilder extends DeletedPostconditionBuilderBase<DeletedPostconditionBuilder>
{
	public DeletedPostcondition build()
	{
		return this.data;
	}
}
