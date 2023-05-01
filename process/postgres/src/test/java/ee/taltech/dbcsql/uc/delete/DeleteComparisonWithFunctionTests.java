package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteComparisonWithFunctionTests extends UseCaseTest
{
	private static final String CONTRACT = """
	operation forget_vardata
	{
	}
	preconditions
	{
		%s
	}
	postconditions
	{
		deleted a;
	}
	""";

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_vardata
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.vardata AS a
		WHERE
			%s
		;
	END;
	""";

	private static final String TEST_PROVIDER_NAME = "PROVIDER";

	@DataProvider(name = TEST_PROVIDER_NAME)
	public Object[][] create()
	{
		return new Object[][]
		{
			{
				"""
				exists vardata a (col_int1 = any('{5, 6}'));
				"""
				,
				"""
				a.col_int1 = any('{5, 6}')
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("insert into vardata (col_int1) values (5), (6), (7)");
						exec("select from forget_vardata()");
						assertQuery(
							"select col_int1 from vardata",
							List.of(List.of(7)),
							"Data matching any must be deleted"
						);
					}
				}
			},
		};
	}

	@Test(dataProvider = TEST_PROVIDER_NAME)
	public void test(String preconditions, String whereClause, Runnable tests)
	{
		this.generateAndCheck(
			new ByteArrayInputStream(String.format(CONTRACT, preconditions).getBytes()),
			String.format(EXPECTED_OUTPUT, whereClause)
		);
		if (tests != null) tests.run();
	}
}
