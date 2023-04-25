package ee.taltech.dbcsql.db.postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ee.taltech.dbcsql.core.db.DBDriver;
import ee.taltech.dbcsql.core.model.db.DBName;
import ee.taltech.dbcsql.core.model.db.DSLName;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder.FKeySubBuilder;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder.TableDefSubBuilder;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;

public class PostgresDriver extends DBDriver
{
	private static final String READ_ALL_TABLE_COLUMN_NAME_TYPES = """
	select
		table_schema,
		table_name,
		column_name,
		data_type
	from
		information_schema.columns
	where
		table_schema not in ('information_schema', 'pg_catalog')
	order by
		table_schema,
		table_name,
		column_name,
		data_type
	""";

	private static final String READ_ALL_CONNECTIONS = """
	select
		o.conname as constraint_name,
		(select nspname from pg_namespace where oid=m.relnamespace) as source_schema,
		m.relname as source_table,
		(select a.attname from pg_attribute a where a.attrelid = m.oid and a.attnum = o.conkey[1] and a.attisdropped = false) as source_column,
		(select nspname from pg_namespace where oid=f.relnamespace) as target_schema,
		f.relname as target_table,
		(select a.attname from pg_attribute a where a.attrelid = f.oid and a.attnum = o.confkey[1] and a.attisdropped = false) as target_column
	from
		pg_constraint o
		left join
			pg_class f
		on
			f.oid = o.confrelid
		left join
			pg_class m
		on
			m.oid = o.conrelid
	where
		o.contype = 'f'
		and o.conrelid in (select oid from pg_class c where c.relkind = 'r')
	order by
		constraint_name,
		source_schema,
		source_table,
		source_column,
		target_schema,
		target_table,
		target_column
	""";

	private static final String READ_ALL_PKEYS = """
	select
		tc.table_schema as schema_name,
		tc.table_name as table_name,
		kc.column_name as column_name
	from
		information_schema.table_constraints tc,
		information_schema.key_column_usage kc
	where
		tc.constraint_type = 'PRIMARY KEY'
		and kc.table_name = tc.table_name
		and kc.table_schema = tc.table_schema
		and kc.constraint_name = tc.constraint_name
		and tc.table_schema not in ('information_schema', 'pg_catalog')
	order by
		schema_name,
		table_name,
		column_name
	""";

	private PostgresTypeInterpreter sqlTypeInterpreter = new PostgresTypeInterpreter();

	public PostgresDriver()
	{
		this(new PostgresConnectionDetails());
	}

	public PostgresDriver(PostgresConnectionDetails details)
	{
		super(details);
	}

	@Override
	protected String implementationDriverName()
	{
		return "postgresql";
	}

	@Override
	public DatabaseDef readDb()
	{
		DatabaseDefBuilder ddb = new DatabaseDefBuilder();
		this
			.readTables(ddb)
			.readConnections(ddb)
			.readPkeys(ddb)
		;
		return ddb.build();
	}

	private DSLName dslName(String schema, String tableName)
	{
		if (schema.equals("public"))
		{
			return new DSLName(tableName);
		}
		return new DSLName(schema + "_" + tableName);
	}

	private PostgresDriver readTables(DatabaseDefBuilder ddb)
	{
		try
		(
			PreparedStatement stmt = this.prepareStatement(READ_ALL_TABLE_COLUMN_NAME_TYPES);
			ResultSet rs = stmt.executeQuery();
		)
		{
			TableDefSubBuilder tdb = null;
			String lastID = null;
			while (rs.next())
			{
				int idx = 0;
				String schemaName = rs.getString(++idx);
				String tableName = rs.getString(++idx);
				String columnName = rs.getString(++idx);
				String type = this.sqlTypeInterpreter.interprete(rs.getString(++idx));
				String fullID = schemaName + "." + tableName;
				if (lastID == null || !lastID.equals(fullID))
				{
					if (lastID != null)
					{
						tdb.build();
					}
					tdb = ddb.makeTable();

					if (!tableName.equals(tableName.toLowerCase())
						|| tableName.contains(" ")
					) {
						tableName = "\"" + tableName + "\"";
					}
					DSLName dslName = this.dslName(schemaName, tableName);
					DBName dbName = new DBName(schemaName, tableName);
					tdb.withName(dslName, dbName);
					lastID = fullID;
				}
				tdb.withColumn(columnName, type);
			}
			tdb.build();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		return this;
	}

	private PostgresDriver readConnections(DatabaseDefBuilder ddb)
	{
		try
		(
			PreparedStatement stmt = this.prepareStatement(READ_ALL_CONNECTIONS);
			ResultSet rs = stmt.executeQuery();
		)
		{
			FKeySubBuilder fkey = null;
			String lastID = null;
			while (rs.next())
			{
				int idx = 0;
				String name = rs.getString(++idx);
				String lSchema = rs.getString(++idx);
				String lTable = rs.getString(++idx);
				String lCol = rs.getString(++idx);
				String rSchema = rs.getString(++idx);
				String rTable = rs.getString(++idx);
				String rCol = rs.getString(++idx);

				String fullID = new StringBuilder()
					.append(name)
					.append(".")
					.append(lSchema)
					.append(".")
					.append(lTable)
					.append(".")
					.append(rSchema)
					.append(".")
					.append(rTable)
					.toString()
				;

				if (lastID == null || !lastID.equals(fullID))
				{
					if (lastID != null)
					{
						fkey.build();
					}
					fkey = ddb.makeConnection();
					fkey.betweenTables(
						new DBName(lSchema, lTable),
						new DBName(rSchema, rTable)
					);
					fkey.withName(name);
					lastID = fullID;
				}

				fkey.connectColumns(lCol, rCol);
			}
			if (fkey != null)
			{
				fkey.build();
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		return this;
	}

	private PostgresDriver readPkeys(DatabaseDefBuilder ddb)
	{
		try
		(
			PreparedStatement stmt = this.prepareStatement(READ_ALL_PKEYS);
			ResultSet rs = stmt.executeQuery();
		)
		{
			while (rs.next())
			{
				int idx = 0;
				String schema = rs.getString(++idx);
				String table = rs.getString(++idx);
				String col = rs.getString(++idx);

				ddb.withIdentifier(new DBName(schema, table), col);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		return this;
	}

	@Override
	public TargetPlatform getTargetPlatform()
	{
		return new PostgresPlatform();
	}
}
