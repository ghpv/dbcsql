package ee.taltech.dbcsql.core.model.dsl.variable;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.db.TableDef;

public class VariableDef
{
	private String alias;
	private TableDef table;

	public VariableDef()
	{
	}

	public VariableDef(String alias, TableDef table)
	{
		this.setAlias(alias);
		this.setTable(table);
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public void setTable(TableDef table)
	{
		this.table = table;
	}

	public String getAlias()
	{
		return alias;
	}
	public TableDef getTable()
	{
		return table;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof VariableDef))
		{
			return super.equals(obj);
		}
		VariableDef other = (VariableDef) obj;
		return true
			&& this.alias.equals(other.alias)
			&& this.table.getAliasedName().equals(other.table.getAliasedName())
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.alias,
			this.table.getAliasedName()
		);
	}

	@Override
	public String toString()
	{
		return table.getAliasedName() + " " + alias;
	}
}
