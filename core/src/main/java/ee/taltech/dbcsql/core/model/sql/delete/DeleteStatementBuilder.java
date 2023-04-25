package ee.taltech.dbcsql.core.model.sql.delete;

public class DeleteStatementBuilder extends DeleteStatementBuilderBase<DeleteStatementBuilder>
{
	public DeleteStatement build()
	{
		return this.data;
	}
}
