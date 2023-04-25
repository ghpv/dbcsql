package ee.taltech.dbcsql.core.model.sql.update;

public class UpdateStatementBuilder extends UpdateStatementBuilderBase<UpdateStatementBuilder>
{
	public UpdateStatement build()
	{
		return this.data;
	}
}
