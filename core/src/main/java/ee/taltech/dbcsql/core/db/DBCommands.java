package ee.taltech.dbcsql.core.db;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;

public class DBCommands
{
	protected DBDriver driver;

	public DBCommands(DBDriver driver)
	{
		this.driver = driver;
	}

	public DatabaseDef readDb()
	{
		return this.driver.readDb();
	}

	public DBCommands execute(String query, Object... args)
	{
		this.driver.execute(query, args);
		return this;
	}
	public DBCommands executeUpdate(String query, Object... args)
	{
		this.driver.executeUpdate(query, args);
		return this;
	}
	public TargetPlatform getTargetPlatform()
	{
		return this.driver.getTargetPlatform();
	}
	public void close()
	{
		this.driver.close();
	}

	public DBDriver getDriver()
	{
		return driver;
	}
}
