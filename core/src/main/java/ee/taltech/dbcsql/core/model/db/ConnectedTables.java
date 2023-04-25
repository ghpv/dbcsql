package ee.taltech.dbcsql.core.model.db;

public class ConnectedTables extends ConnectedValues<TableDef>
{
	public ConnectedTables(TableDef a, TableDef b)
	{
		super(a, b);
	}

	@Override
	protected int compare(TableDef a, TableDef b)
	{
		return a.getAliasedName().compareTo(b.getAliasedName());
	}
}
