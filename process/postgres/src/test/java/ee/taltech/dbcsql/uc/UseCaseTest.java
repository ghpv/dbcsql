package ee.taltech.dbcsql.uc;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.input.InputPhase;
import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.core.phase.output.OutputPhase;
import ee.taltech.dbcsql.core.phase.output.persistence.MemoryPersistence;
import ee.taltech.dbcsql.test.SQLStatementAssertions;
import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class UseCaseTest
{
	protected PostgresTestDriver driver;
	protected DBTestCommands db;

	@BeforeMethod
	public void init()
	{
		this.driver = new PostgresTestDriver();
		this.db = new DBTestCommands(this.driver);
		this.createTables();
	}

	@AfterMethod
	public void teardown()
	{
		this.db.close();
	}

	public void createTables()
	{
		this.db.loadTestDatabase();
	}

	public MemoryPersistence generate(InputStream contract)
	{
		MemoryPersistence persistence = new MemoryPersistence();
		InputPhase input = new InputPhase();
		GenerationContextSubBuilder contextBuilder = input
			.makeGenerationContext()
				.withDBConnection(this.driver)
				.makeCombinedPersistence()
					.withPersistence(persistence)
					.withDatabasePersistence()
				.build()
		;
		this.loadContext(contextBuilder);
		this.extraOperationsOnContext(contextBuilder);
		contextBuilder.build();

		this.extraOperationsOnInputPhase(input);

		GenerationRequest request = input
			.withContractStream(contract)
			.finishInput()
		;

		new OutputPhase()
			.processRequest(request)
		;

		return persistence;
	}

	public void check(MemoryPersistence persistence, String expectedSQL)
	{
		String actual = persistence.getMemory().toString();
		SQLStatementAssertions.assertStatementsEqual(actual, expectedSQL);
	}

	public void generateAndCheck(InputStream contract, String expectedSQL)
	{
		MemoryPersistence persistence = this.generate(contract);
		this.check(persistence, expectedSQL);
	}

	protected void extraOperationsOnContext(GenerationContextSubBuilder contextBuilder)
	{
	}
	protected void extraOperationsOnInputPhase(InputPhase input)
	{
	}

	protected void loadContext(GenerationContextSubBuilder builder)
	{
		builder.withContextFromDB();
	}

	public UseCaseTest exec(String qry, Object... args)
	{
		this.db.execute(qry, args);
		return this;
	}

	public UseCaseTest assertQuery(List<List<Object>> qry, List<List<Object>> expected, String msg)
	{
		Assert.assertEquals(
			qry,
			expected,
			msg
		);
		return this;
	}

	public UseCaseTest assertQuery(String qry, List<List<Object>> expected, String msg)
	{
		return this.assertQuery(this.db.readQuery(qry), expected, msg);
	}

	public UseCaseTest assertQueryAsStr(String qry, List<List<Object>> expected, String msg)
	{
		Assert.assertEquals(
			toListListString(this.db.readQuery(qry)),
			toListListString(expected),
			msg
		);
		return this;
	}

	private List<List<String>> toListListString(List<List<Object>> llo)
	{
		return llo
			.stream()
			.map(x -> x
				.stream()
				.map(y -> (y == null ? null : y.toString()))
				.collect(Collectors.toList())
			)
			.collect(Collectors.toList())
		;
	}
}
