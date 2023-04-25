package ee.taltech.dbcsql.core.model.db;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class DatabaseDefBuilder
{
	protected DatabaseDef data = new DatabaseDef();

	public class TableDefSubBuilder extends TableDefBuilderBase<TableDefSubBuilder>
	{
		protected DatabaseDefBuilder owner;

		private TableDefSubBuilder(DatabaseDefBuilder owner)
		{
			this.owner = owner;
		}

		public DatabaseDefBuilder build()
		{
			this
				.owner
				.withTable(this.data)
			;

			return this.owner;
		}
	}

	public DatabaseDefBuilder withTable(TableDef table)
	{
		this
			.data
			.addTable(table);
		return this;
	}

	public TableDefSubBuilder makeTable()
	{
		return new TableDefSubBuilder(this);
	}

	public TableDefSubBuilder makeExtendingTable(DBName parentName)
	{
		TableDefSubBuilder ret = new TableDefSubBuilder(this);
		TableDef td = this.data.getTable(parentName);
		if (td == null)
		{
			throw new TranslatorInputException("Did not find table to extend: '" + parentName + "'");
		}
		ret.withParentTable(td);
		return ret;
	}

	public class FKeySubBuilder extends FKeyBaseBuilder<FKeySubBuilder>
	{
		private DatabaseDefBuilder owner;

		public FKeySubBuilder(DatabaseDefBuilder owner)
		{
			this.owner = owner;
		}

		public FKeySubBuilder betweenTables(DBName left, DBName right)
		{
			TableDef leftTable = this.findTable(left);
			TableDef rightTable = this.findTable(right);
			this.betweenTables(leftTable, rightTable);
			return this;
		}

		public FKeySubBuilder betweenTables(String left, String right)
		{
			return this.betweenTables(new DBName(left), new DBName(right));
		}

		private TableDef findTable(DBName name)
		{
			TableDef ret = this.owner.data.getTable(name);
			if (ret == null)
			{
				throw new TranslatorInputException("Table not found for DB name '" + name + "'");
			}
			return ret;
		}

		public DatabaseDefBuilder build()
		{
			this.owner.withConnection(this.data);
			return this.owner;
		}
	}

	public DatabaseDefBuilder withConnection(FKey fkey)
	{
		this.data.addConnection(fkey);
		return this;
	}

	public FKeySubBuilder makeConnection()
	{
		return new FKeySubBuilder(this);
	}

	public DatabaseDefBuilder withIdentifier(DBName table, String column)
	{
		if (this.data.getTable(table) == null)
		{
			throw new TranslatorInputException("No such table '" + table + "'");
		}
		TableDef tbl = this.data.getTable(table);
		if (!tbl.hasColumnDB(column))
		{
			throw new TranslatorInputException("Table '" + table + "' has no column '" + column + "' to use as identifier");
		}
		tbl.setIdentifier(new PKey(tbl.getColumnDB(column)));
		return this;
	}

	public DatabaseDef build()
	{
		return this.data;
	}
}
