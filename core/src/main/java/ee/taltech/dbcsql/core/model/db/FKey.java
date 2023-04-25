package ee.taltech.dbcsql.core.model.db;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FKey
{
	public static final String DEFAULT_NAME = "";

	private ConnectedTables tables;
	private List<ConnectedColumns> connections = new LinkedList<>();
	private String name = DEFAULT_NAME;

	public FKey()
	{
	}

	public FKey(TableDef a, List<ColumnDef> aCols, TableDef b, List<ColumnDef> bCols)
	{
		this.setConnectedTables(a, b);
		assert aCols.size() == bCols.size(): "Connection columns can't be inequal";
		Iterator<ColumnDef> ai = aCols.iterator();
		Iterator<ColumnDef> bi = bCols.iterator();

		while (ai.hasNext())
		{
			this.addConnection(ai.next(), bi.next());
		}
	}

	public FKey(String name, TableDef a, List<ColumnDef> aCols, TableDef b, List<ColumnDef> bCols)
	{
		this(a, aCols, b, bCols);
		this.setName(name);
	}

	public List<ConnectedColumns> getConnections()
	{
		return Collections.unmodifiableList(this.connections);
	}

	public TableDef getA()
	{
		return tables.getA();
	}

	public TableDef getB()
	{
		return tables.getB();
	}

	public void setConnectedTables(TableDef a, TableDef b)
	{
		this.tables = new ConnectedTables(a, b);
		this.connections = new LinkedList<>();
	}

	public void addConnection(ColumnDef a, ColumnDef b)
	{
		this.connections.add(new ConnectedColumns(a, b));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (ConnectedColumns cc: this.connections)
		{
			sb
				.append(this.name)
				.append(": ")
				.append(this.getA().getAliasedName())
				.append(".")
				.append(cc.getA().getAliasedName())
				.append(" = ")
				.append(this.getB().getAliasedName())
				.append(".")
				.append(cc.getB().getAliasedName())
			;
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof FKey))
		{
			return super.equals(obj);
		}
		FKey other = (FKey) obj;
		return true
			&& this.tables.equals(other.tables)
			&& this.connections.equals(other.connections)
			&& this.name.equals(other.name)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.tables,
			this.name,
			this.connections.hashCode()
		);
	}
}
