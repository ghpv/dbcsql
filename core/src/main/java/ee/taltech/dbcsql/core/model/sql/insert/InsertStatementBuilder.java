package ee.taltech.dbcsql.core.model.sql.insert;

public class InsertStatementBuilder extends InsertStatementBuilderBase<InsertStatementBuilder>
{
	public InsertStatement build()
	{
		return this.data;
	}
}
