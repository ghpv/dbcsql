package ee.taltech.dbcsql.test;

import org.testng.annotations.BeforeMethod;

import ee.taltech.dbcsql.core.model.db.DBName;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDefBuilder;
import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.GenerationContextBuilder;
import ee.taltech.dbcsql.core.phase.output.RestrictionRegistry;
import ee.taltech.dbcsql.core.phase.output.persistence.MemoryPersistence;
import ee.taltech.dbcsql.db.postgres.PostgresPlatform;
import ee.taltech.dbcsql.process.postgres.PostgresOutputGenerator;
import ee.taltech.dbcsql.process.postgres.StatementRenderer;
import ee.taltech.dbcsql.process.postgres.translate.ModelTranslator;

public class IntegrationTest extends DBTest
{
	protected DatabaseDef dbDef;
	protected TableDef testTable;
	protected VariableDef testVar;
	protected MemoryPersistence persistence;
	protected GenerationContext ctx;
	protected RestrictionRegistry reg;
	protected PostgresOutputGenerator outGen;
	protected StatementRenderer condGen;

	@BeforeMethod
	public void init()
	{
		this.persistence = new MemoryPersistence();
		this.ctx = new GenerationContextBuilder()
			.withPersistence(persistence)
			.withPlatform(new PostgresPlatform())
			.withDatabase(this.dbDef)
			.build()
		;
		this.reg = ctx.getRestrictionRegistry();
		this.outGen = new PostgresOutputGenerator(ctx);
		this.condGen = new StatementRenderer();

		this.db.execute("""
		CREATE TABLE TEST
		(
			ID SERIAL,
			DATA TEXT
		)
		""");
		this.dbDef = this.db.readDb();
		this.testTable = this.dbDef.getTable(new DBName("public", "test"));
		this.testVar = new VariableDefBuilder()
			.withAlias("a")
			.withTable(this.testTable)
			.build()
		;
	}

	protected String generateCondition(StatementRenderer generator, Statement st)
	{
		this
			.persistence
			.startNew()
			.write(st.accept(generator))
			.finished()
		;
		return this.persistence.getMemory().toString();
	}

	protected String generateCondition(PostconditionDef condition)
	{
		Statement st = new ModelTranslator(this.ctx).translatePostcondition(condition);
		return this.generateCondition(this.condGen, st);
	}
}
