package ee.taltech.dbcsql.core.model.db;

import java.util.Objects;

public class AliasedName implements Comparable<AliasedName>
{
	private DSLName dslName = new DSLName("");
	private DBName dbName = new DBName("");

	public AliasedName()
	{
	}

	public AliasedName(String dslName, String dbName)
	{
		this.setDBName(dslName);
		this.setDSLName(dbName);
	}
	public AliasedName(String name)
	{
		this(name, name);
	}

	public DBName getDBName()
	{
		return dbName;
	}

	public void setName(DBName name)
	{
		this.dbName = name;
	}
	public void setDBName(String name)
	{
		this.setName(new DBName(name));
	}

	public DSLName getDSLName()
	{
		return dslName;
	}
	public void setName(DSLName name)
	{
		this.dslName = name;
	}
	public void setDSLName(String name)
	{
		this.setName(new DSLName(name));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AliasedName))
		{
			return super.equals(obj);
		}
		AliasedName other = (AliasedName) obj;
		return true
			&& this.dslName.equals(other.dslName)
			&& this.dbName.equals(other.dbName)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.dslName,
			this.dbName
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.dslName)
			.append("(")
			.append(this.dbName)
			.append(")")
			.toString()
		;
	}

	@Override
	public int compareTo(AliasedName other)
	{
		int db = this.getDBName().compareTo(other.getDBName());
		if (db != 0)
		{
			return db;
		}
		return this.getDSLName().compareTo(other.getDSLName());
	}
}
