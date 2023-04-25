package ee.taltech.dbcsql.core.model.db;

public class ConnectedColumns extends ConnectedValues<ColumnDef>
{
	public ConnectedColumns(ColumnDef a, ColumnDef b)
	{
		super(a, b);
	}

	@Override
	protected int compare(ColumnDef a, ColumnDef b)
	{
		return a.getTable().getAliasedName().compareTo(b.getTable().getAliasedName());
	}

	public ColumnDef getOneBelongingTo(TableDef table)
	{
		if (this.getA().getTable().getAliasedName().equals(table.getAliasedName()))
		{
			return this.getA();
		}
		if (this.getB().getTable().getAliasedName().equals(table.getAliasedName()))
		{
			return this.getB();
		}
		throw new RuntimeException("Table " + table.getAliasedName() + " does not own " + this.getA() + " nor " + this.getB());
	}

	public ColumnDef getOneNotBelongingTo(TableDef table)
	{
		ColumnDef ret = this.getOneBelongingTo(table);
		if (this.getA().getTable().getAliasedName().equals(ret.getTable().getAliasedName()))
		{
			return this.getB();
		}
		return this.getA();
	}
}
