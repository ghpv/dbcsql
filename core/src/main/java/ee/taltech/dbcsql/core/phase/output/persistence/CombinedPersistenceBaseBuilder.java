package ee.taltech.dbcsql.core.phase.output.persistence;

import ee.taltech.dbcsql.core.db.DBDriver;

public class CombinedPersistenceBaseBuilder<BuilderT extends CombinedPersistenceBaseBuilder<BuilderT>>
{
	protected CombinedPersistence data = new CombinedPersistence();

	@SuppressWarnings("unchecked")
	public BuilderT withPersistence(Persistence p)
	{
		this.data.addPersistence(p);
		return (BuilderT) this;
	}

	public BuilderT withPersistenceInMemory()
	{
		return this.withPersistence(new MemoryPersistence());
	}

	public BuilderT withDatabasePersistence(DBDriver driver)
	{
		return this.withPersistence(new DatabasePersistence(driver));
	}
}
