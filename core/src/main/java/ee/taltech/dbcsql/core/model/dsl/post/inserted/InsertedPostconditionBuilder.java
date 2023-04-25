package ee.taltech.dbcsql.core.model.dsl.post.inserted;

public class InsertedPostconditionBuilder extends InsertedPostconditionBuilderBase<InsertedPostconditionBuilder>
{
	public InsertedPostcondition build()
	{
		return this.data;
	}
}
