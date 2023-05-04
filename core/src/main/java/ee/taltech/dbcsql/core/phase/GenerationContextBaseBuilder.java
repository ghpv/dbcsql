package ee.taltech.dbcsql.core.phase;

import java.io.InputStream;

import ee.taltech.dbcsql.core.db.DBCommands;
import ee.taltech.dbcsql.core.db.DBDriver;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;
import ee.taltech.dbcsql.core.phase.input.context.ContextInputParser;
import ee.taltech.dbcsql.core.phase.output.persistence.CombinedPersistenceBaseBuilder;
import ee.taltech.dbcsql.core.phase.output.persistence.Persistence;

public class GenerationContextBaseBuilder<BuilderT extends GenerationContextBaseBuilder<BuilderT>>
{
	protected GenerationContext data = new GenerationContext();

	@SuppressWarnings("unchecked")
	public BuilderT withPersistence(Persistence persistence)
	{
		this.data.persistence = persistence;
		return (BuilderT) this;
	}

	public class CombinedPersistenceSubBuilder extends CombinedPersistenceBaseBuilder<CombinedPersistenceSubBuilder>
	{
		private BuilderT owner;

		public CombinedPersistenceSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public CombinedPersistenceSubBuilder withDatabasePersistence()
		{
			this.withDatabasePersistence(this
				.owner
				.data
				.getDatabase()
				.getDriver()
			);
			return this;
		}

		public BuilderT build()
		{
			this.owner.withPersistence(this.data);
			return this.owner;
		}
	};

	@SuppressWarnings("unchecked")
	public CombinedPersistenceSubBuilder makeCombinedPersistence()
	{
		return new CombinedPersistenceSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withPlatform(TargetPlatform platform)
	{
		this.data.platform = platform;
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withDatabase(DatabaseDef db)
	{
		this.data.databaseDef = db;
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withContextFromDB()
	{
		assert this.data.database != null: "DB must given if we are to get context from it";
		this.withDatabase(this.data.database.readDb());

		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withContextFromStream(InputStream stream)
	{
		this.withDatabase(new ContextInputParser().parse(stream));


		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withDBConnection(DBDriver driver)
	{
		this.data.database = new DBCommands(driver);
		this.withPlatform(this.data.database.getTargetPlatform());
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withReturnOnLastPostcondition(boolean b)
	{
		this.data.setReturnLastPostcondition(b);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withSecurityInvoker(boolean b)
	{
		this.data.setSecurityInvoker(b);
		return (BuilderT) this;
	}


	@SuppressWarnings("unchecked")
	public BuilderT withParameterPrefix(String pfx)
	{
		this.data.getParameterMender().setPrefix(pfx);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withFunctionPrefix(String pfx)
	{
		this.data.getFunctionMender().setPrefix(pfx);
		return (BuilderT) this;
	}
}
