package ee.taltech.dbcsql.db.postgres;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class PostgresTestDriverTests
{
	@Test
	public void noExceptionOnConnectionCreation()
	{
		new PostgresTestDriver();
	}

	@Test
	public void noTablesOnNew()
	{
		DBTestCommands dbc = new DBTestCommands();
		Assert.assertEquals(dbc.getUserTables(), Set.of());
		dbc.executeUpdate("create table test(id serial primary key)");
		Assert.assertEquals(dbc.getUserTables(), Set.of("test"));

		dbc = new DBTestCommands();
		Assert.assertEquals(dbc.getUserTables(), Set.of(), "New instance of DBCommands must reset database");
	}

	@Test
	public void noFunctionsOnNew()
	{
		DBTestCommands dbc = new DBTestCommands();
		dbc.execute("""
		CREATE FUNCTION add
		(
			a integer,
			b integer
		)
		RETURNS integer
		LANGUAGE SQL
		IMMUTABLE
		RETURNS NULL ON NULL INPUT
		RETURN a + b;
		""");
		Assert.assertEquals(dbc.getUserFunctions(), Set.of("add"));

		dbc = new DBTestCommands();
		Assert.assertEquals(dbc.getUserFunctions(), Set.of());
	}
}
