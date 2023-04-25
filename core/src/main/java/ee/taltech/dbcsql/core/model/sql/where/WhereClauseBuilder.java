package ee.taltech.dbcsql.core.model.sql.where;

public class WhereClauseBuilder extends WhereClauseBuilderBase<WhereClauseBuilder>
{
	public WhereClause build()
	{
		return this.data;
	}
}
