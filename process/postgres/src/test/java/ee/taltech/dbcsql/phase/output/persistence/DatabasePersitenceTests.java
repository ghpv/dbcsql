package ee.taltech.dbcsql.phase.output.persistence;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.output.persistence.DatabasePersistence;
import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class DatabasePersitenceTests
{
	@Test
	public void testPersitenceWrite()
	{
		PostgresTestDriver driver = new PostgresTestDriver();
		DatabasePersistence pers = new DatabasePersistence(driver);
		driver.execute("create table test(id int)");
		pers
			.startNew()
			.write("insert into test values (1)")
			.finished()
		;
		Assert.assertEquals(
			new DBTestCommands(driver).readQuery("select * from test"),
			List.of(List.of(1)),
			"Persistence query was executed"
		);
	}
}
