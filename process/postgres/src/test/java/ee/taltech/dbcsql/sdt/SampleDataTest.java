package ee.taltech.dbcsql.sdt;

import java.io.InputStream;

import org.testng.annotations.BeforeMethod;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class SampleDataTest extends UseCaseTest
{
	private static final String PREFIX = "sdt/";
	private static final String SAMPLE_DATA_CONTEXT = "context.dsl";
	protected SampleDatabase sdb;

	@BeforeMethod
	public void setupSampleDataTest()
	{
		this.sdb = new SampleDatabase(this.db);
	}

	@Override
	protected void extraOperationsOnContext(GenerationContextSubBuilder contextBuilder)
	{
		contextBuilder.withReturnOnLastPostcondition(true);
		super.extraOperationsOnContext(contextBuilder);
	}

	@Override
	protected void loadContext(GenerationContextSubBuilder builder)
	{
		builder
			.withContextFromStream(this.getClasspathResource(SAMPLE_DATA_CONTEXT))
		;
	}

	@Override
	public void createTables()
	{
		this.db.loadSampleDataDatabase();
	}

	public void generateAndCheck(String classpathContractFile, String expectedSQL)
	{
		super.generateAndCheck(this.getClasspathResource(classpathContractFile), expectedSQL);
	}

	private InputStream getClasspathResource(String classpathName)
	{
		InputStream stream = this
			.getClass()
			.getClassLoader()
			.getResourceAsStream(PREFIX + classpathName)
		;
		assert stream != null: "Stream not found: " + classpathName;
		return stream;
	}
}
