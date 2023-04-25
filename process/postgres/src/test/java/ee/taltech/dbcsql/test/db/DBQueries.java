package ee.taltech.dbcsql.test.db;

public class DBQueries
{
	public static final String USER_TABLES = """
	select
		table_name
	from
		information_schema.tables
	where
		table_schema not in ('information_schema', 'pg_catalog')
		and table_type = 'BASE TABLE'
	""";

	public static final String USER_FUNCTIONS = """
	select
		routine_name
	from
		information_schema.routines
	where
		routine_schema not in ('information_schema', 'pg_catalog')
	""";
}
