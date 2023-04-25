package ee.taltech.dbcsql.core.model.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TableDef
{
	private AliasedName name = new AliasedName();
	private Map<DSLName, ColumnDef> cols = new HashMap<>();
	private Map<DBName, DSLName> nameMap = new HashMap<>();
	private Optional<PKey> identifier = Optional.empty();

	public TableDef()
	{
	}

	public TableDef(String dslName, String dbName, Collection<ColumnDef> cols)
	{
		this.setName(new DSLName(dslName));
		this.setName(new DBName(dbName));
		this.addColumns(cols);
	}
	public TableDef(String name, Collection<ColumnDef> cols)
	{
		this(name, name, cols);
	}

	public TableDef clone()
	{
		TableDef ret = new TableDef();
		ret.setName(this.getAliasedName().getDBName());
		ret.setName(this.getAliasedName().getDSLName());
		ret.setIdentifier(this.getIdentifier());
		for (ColumnDef cdef: this.cols.values())
		{
			ret.addColumn(cdef.clone());
		}
		return ret;
	}

	public AliasedName getAliasedName()
	{
		return name;
	}

	public void setNameDB(String name)
	{
		this.setName(new DBName(name));
	}
	public void setName(DBName name)
	{
		this.name.setName(name);
	}

	public void setNameDSL(String name)
	{
		this.setName(new DSLName(name));
	}
	public void setName(DSLName name)
	{
		this.name.setName(name);
	}

	public void setName(String name)
	{
		this.setName(new DBName(name));
		this.setName(new DSLName(name));
	}

	public boolean hasColumn(ColumnDef col)
	{
		return this.getAliasedName().equals(col.getTable().getAliasedName());
	}

	public boolean hasColumn(DBName dbName)
	{
		return this.nameMap.containsKey(dbName);
	}

	public boolean hasColumnDB(String dbName)
	{
		return this.hasColumn(new DBName(dbName));
	}

	public boolean hasColumnDSL(String dslName)
	{
		return this.cols.containsKey(new DSLName(dslName));
	}

	public void addColumns(Collection<ColumnDef> cols)
	{
		cols
			.stream()
			.forEach(x -> this.addColumn(x))
		;
	}

	public void addColumn(ColumnDef col)
	{
		AliasedName name = col.getAliasedName();
		col.setTable(this);
		this.cols.put(name.getDSLName(), col);
		this.nameMap.put(name.getDBName(), name.getDSLName());
	}

	public ColumnDef getColumnDSL(String name)
	{
		return this.getColumn(new DSLName(name));
	}

	public ColumnDef getColumn(DSLName dslName)
	{
		ColumnDef data = this.cols.get(dslName);
		assert data != null: "Did not find DSL column for " + dslName;
		return data;
	}

	public ColumnDef getColumnDB(String name)
	{
		return this.getColumn(new DBName(name));
	}

	public ColumnDef getColumn(DBName dbName)
	{
		DSLName dslName = this.nameMap.get(dbName);
		assert dslName != null: "Did not find DSL name for DB name " + dbName + " in table " + this.name;
		return this.getColumn(dslName);
	}

	public Optional<PKey> getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(PKey identifier)
	{
		this.setIdentifier(Optional.of(identifier));
	}
	public void setIdentifier(Optional<PKey> identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TableDef))
		{
			return super.equals(obj);
		}
		TableDef other = (TableDef) obj;
		return true
			&& this.name.equals(other.name)
			&& this.cols.equals(other.cols)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.name,
			this.cols.hashCode()
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("Table ")
			.append(this.name)
			.append(" with cols ")
			.append(this.cols)
			.toString()
		;
	}
}
