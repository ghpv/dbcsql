package ee.taltech.dbcsql.test;

import org.testng.annotations.BeforeMethod;

import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class DBTest
{
	protected PostgresTestDriver driver;
	protected DBTestCommands db;

	@BeforeMethod
	public void setup()
	{
		this.driver = new PostgresTestDriver();
		this.db = new DBTestCommands(this.driver);
	}
}
