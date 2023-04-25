package ee.taltech.dbcsql.db;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class DBTests
{
	@Test
	public void testReadGeneric()
	{
		DBTestCommands db = new DBTestCommands(new PostgresTestDriver());
		db.execute("create table test (id integer, data text)");
		db.execute("insert into test values (1, 'wow'), (2, 'no')");
		Assert.assertEquals(
			db.readQuery("select * from test"),
			List.of(List.of(1, "wow"), List.of(2, "no")),
			"Value must match what was just inserted"
		);
	}
}
