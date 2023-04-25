package ee.taltech.dbcsql.core.model.db;

import java.util.Objects;

public class ColumnDef
{
	private AliasedName name = new AliasedName();
	private TableDef table;
	private String type;

	public ColumnDef(TableDef table, String dslName, String dbName, String type)
	{
		this(table, new DSLName(dslName), new DBName(dbName), type);
	}
	public ColumnDef(TableDef table, DSLName dslName, DBName dbName, String type)
	{
		this.setTable(table);
		this.setName(dslName);
		this.setName(dbName);
		this.setType(type);
	}

	public ColumnDef(TableDef table, String name, String type)
	{
		this(table, name, name, type);
	}

	public ColumnDef clone()
	{
		return new ColumnDef
		(
			this.getTable(),
			this.getAliasedName().getDSLName(),
			this.getAliasedName().getDBName(),
			this.getType()
		);
	}

	public void setName(DSLName name)
	{
		this.name.setName(name);
	}

	public void setName(DBName name)
	{
		this.name.setName(name);
	}
	public void setTable(TableDef table)
	{
		this.table = table;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public AliasedName getAliasedName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public TableDef getTable()
	{
		return table;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ColumnDef))
		{
			return super.equals(obj);
		}
		ColumnDef other = (ColumnDef) obj;
		return true
			&& this.table.getAliasedName().equals(other.table.getAliasedName())
			&& this.name.equals(other.name)
			&& this.type.equals(other.type)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.table.getAliasedName(),
			this.name,
			this.type
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.name)
			.append(" ")
			.append(this.type)
			.toString()
		;
	}
}
