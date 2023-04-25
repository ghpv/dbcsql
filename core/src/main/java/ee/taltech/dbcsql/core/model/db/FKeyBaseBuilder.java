package ee.taltech.dbcsql.core.model.db;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class FKeyBaseBuilder<BuilderT extends FKeyBaseBuilder<BuilderT>>
{
	protected FKey data = new FKey();
	private TableDef left;
	private TableDef right;

	@SuppressWarnings("unchecked")
	public BuilderT betweenTables(TableDef left, TableDef right)
	{
		this.data.setConnectedTables(left, right);
		this.left = left;
		this.right = right;
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT connectColumns(String left, String right)
	{
		this.connectColumns(
			this.findColumn(this.left, left),
			this.findColumn(this.right, right)
		);
		return (BuilderT) this;
	}

	private ColumnDef findColumn(TableDef table, String column)
	{
		DBName colname = new DBName(column);

		if (!table.hasColumn(colname))
		{
			throw new TranslatorInputException("Column '" + column + "' in table " + table.getAliasedName() + " is not found");
		}
		ColumnDef ret = table.getColumn(new DBName(column));
		return ret;
	}

	@SuppressWarnings("unchecked")
	public BuilderT connectColumns(ColumnDef left, ColumnDef right)
	{
		this.data.addConnection(left, right);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withName(String name)
	{
		this.data.setName(name);
		return (BuilderT) this;
	}
}
