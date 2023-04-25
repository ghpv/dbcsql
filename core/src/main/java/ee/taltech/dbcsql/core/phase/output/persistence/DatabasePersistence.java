package ee.taltech.dbcsql.core.phase.output.persistence;

import ee.taltech.dbcsql.core.db.DBDriver;

/**
 * Write generated contracts to the database.
 */
public class DatabasePersistence implements Persistence
{
	private MemoryPersistence memory = new MemoryPersistence();
	private DBDriver driver = null;

	public DatabasePersistence(DBDriver driver)
	{
		this.driver = driver;
	}

	private void writeToDB()
	{
		String qry = this
			.memory
			.getMemory()
			.toString()
		;
		this.driver.execute(qry);
	}

	@Override
	public Persistence startNew(String contractName)
	{
		this.memory.startNew(contractName);
		return this;
	}

	public Persistence startNew()
	{
		this.startNew(null);
		return this;
	}

	@Override
	public Persistence finished()
	{
		this.memory.finished();
		if (this.memory.getMemory() != null)
		{
			this.writeToDB();
		}
		return this;
	}

	@Override
	public Persistence write(String s)
	{
		this.memory.write(s);
		return this;
	}
}
