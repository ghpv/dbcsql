package ee.taltech.dbcsql.core.model.db;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DatabaseDef
{
	private Map<DSLName, TableDef> tables = new HashMap<>();
	private Map<ConnectedStrings, Map<String, FKey>> connections = new HashMap<>();
	private Map<String, Set<String>> connectionKeyCache = new HashMap<>();
	private Map<DBName, DSLName> nameMap = new HashMap<>();

	public DatabaseDef()
	{
	}

	public DatabaseDef(Set<TableDef> tables, Set<FKey> connections)
	{
		this.addTables(tables);
		this.addConnections(connections);
	}

	public DatabaseDef(Set<TableDef> tables)
	{
		this(tables, Set.of());
	}

	public DatabaseDef addTables(Collection<TableDef> tables)
	{
		tables
			.stream()
			.forEach(this::addTable)
		;
		return this;
	}

	public DatabaseDef addTable(TableDef table)
	{
		AliasedName name = table.getAliasedName();
		this
			.tables
			.put(name.getDSLName(), table);
		this.nameMap.put(name.getDBName(), name.getDSLName());
		return this;
	}

	public DatabaseDef addConnections(Collection<FKey> connections)
	{
		return this;
	}

	public DatabaseDef addConnection(FKey connection)
	{
		ConnectedStrings key = this.getConnectionKey(connection.getA(), connection.getB());
		this
			.connections
			.computeIfAbsent(key, x -> new HashMap<>())
			.put(connection.getName(), connection)
		;
		String a = key.getA();
		String b = key.getB();
		this
			.addOneWayKeyConnection(a, b)
			.addOneWayKeyConnection(b, a)
		;
		return this;
	}
	private DatabaseDef addOneWayKeyConnection(String a, String b)
	{
		this
			.connectionKeyCache
			.computeIfAbsent(a, x -> new HashSet<>())
			.add(b)
		;
		return this;
	}
	private ConnectedStrings getConnectionKey(TableDef a, TableDef b)
	{
		return this.getConnectionKey(a.getAliasedName().getDBName(), b.getAliasedName().getDBName());
	}
	private ConnectedStrings getConnectionKey(DBName a, DBName b)
	{
		return this.getConnectionKey(connectionKey(a), connectionKey(b));
	}
	private String connectionKey(DBName n)
	{
		return n.toString();
	}
	private ConnectedStrings getConnectionKey(String a, String b)
	{
		return new ConnectedStrings(a, b);
	}

	public TableDef getTableDSL(String name)
	{
		return this.getTable(new DSLName(name));
	}
	public TableDef getTable(DSLName dslName)
	{
		return this.tables.get(dslName);
	}
	public TableDef getTableDB(String name)
	{
		return this.getTable(new DBName(name));
	}
	public TableDef getTable(DBName dbName)
	{
		return this.getTable(this.nameMap.get(dbName));
	}

	public List<FKey> getConnection(TableDef a, TableDef b)
	{
		return this.getConnection(tableName(a), tableName(b));
	}
	public List<FKey> getConnection(TableDef a, TableDef b, String name)
	{
		return this.getConnection(tableName(a), tableName(b), name);
	}
	public List<FKey> getConnection(TableDef a, TableDef b, List<String> names)
	{
		return this.getConnection(tableName(a), tableName(b), names);
	}
	private DBName tableName(TableDef a)
	{
		return a.getAliasedName().getDBName();
	}
	public List<FKey> getConnection(String a, String b, String... names)
	{
		return this.getConnection(new DBName(a), new DBName(b), Arrays.asList(names));
	}
	public List<FKey> getConnection(DBName a, DBName b, String... names)
	{
		return this.getConnection(a, b, Arrays.asList(names));
	}
	public List<FKey> getConnection(DBName a, DBName b, List<String> names)
	{
		LinkedList<String> nameList = new LinkedList<>();
		for (String n: names)
		{
			nameList.add(n);
		}
		return this.getConnection(a, b, (Queue<String>) nameList);
	}
	public List<FKey> getConnection(DBName a, DBName b, Queue<String> names)
	{
		List<FKey> ret = new LinkedList<>();
		String curName = null;
		for (ConnectedStrings c: this.findPath(a, b))
		{
			if (curName == null && !names.isEmpty())
			{
				curName = names.poll();
			}

			FKey key = this.getNamedConnection(c, curName);
			if (key == null)
			{
				key = this.getFirstConnection(c);
			}
			else
			{
				curName = null;
			}
			ret.add(key);
		}
		return ret;
	}
	private List<ConnectedStrings> findPath(DBName a, DBName b)
	{
		Queue<String> q = new LinkedList<>();
		Set<String> seen = new HashSet<>();
		Map<String, String> trace = new HashMap<>();
		String keyA = connectionKey(a);
		String keyB = connectionKey(b);
		q.add(keyA);
		seen.add(keyA);
		while (!q.isEmpty())
		{
			String cur = q.poll();
			Set<String> children = this.connectionKeyCache.getOrDefault(cur, new HashSet<>());
			if (children.contains(keyB))
			{
				trace.put(keyB, cur);
				break;
			}
			for (String child: children)
			{
				if (!seen.add(child))
				{
					continue;
				}
				trace.put(child, cur);
				q.add(child);
			}
		}
		String cur = keyB;
		String tback = trace.get(cur);
		List<ConnectedStrings> ret = new LinkedList<>();
		while (tback != null)
		{
			ret.add(this.getConnectionKey(cur, tback));
			cur = tback;
			tback = trace.get(cur);
		}
		Collections.reverse(ret);
		return ret;
	}
	private Map<String, FKey> getConnectionMap(ConnectedStrings key)
	{
		return this
			.connections
			.get(key)
		;
	}
	private FKey getFirstConnection(ConnectedStrings key)
	{
		return this
			.getConnectionMap(key)
			.values()
			.iterator()
			.next()
		;
	}
	private FKey getNamedConnection(ConnectedStrings key, String name)
	{
		return this
			.getConnectionMap(key)
			.get(name)
		;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DatabaseDef))
		{
			return super.equals(obj);
		}
		DatabaseDef other = (DatabaseDef) obj;
		return true
			&& this.tables.equals(other.tables)
			&& this.connections.equals(other.connections)
		;
	}

	@Override
	public int hashCode()
	{
		return this.tables.hashCode();
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("Database: [")
			.append(this.tables.values().toString())
			.append("] and connections: [")
			.append(this.connections)
			.append("]")
			.toString()
		;
	}
}
