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
		int start = 0;
		int idx = -1;
		while (true)
		{
			idx = qry.indexOf("END;", start);
			if (idx != -1)
			{
				idx += 4;
			}
			else
			{
				break;
			}
			String subqry = qry.substring(start, idx);
			start = idx;
			this.driver.execute(subqry);
		}
		String subqry = qry.substring(start);
		if (!subqry.strip().equals(""))
		{
			this.driver.execute(subqry);
		}
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
