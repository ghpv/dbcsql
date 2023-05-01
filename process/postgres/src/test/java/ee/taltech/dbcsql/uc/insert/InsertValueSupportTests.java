package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertValueSupportTests extends UseCaseTest
{
	private static final String CONTRACT = """
	operation insert_vardata
	{
	}
	preconditions
	{
	}
	postconditions
	{
		inserted vardata a
		{
			%s
		};
	}
	""";

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION insert_vardata
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.vardata AS a
		(
			%s
		)
		VALUES
		(
			%s
		)
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
				col_timestamp1 = current_timestamp;
				"""
				,
				"""
				col_timestamp1
				"""
				,
				"""
				current_timestamp
				"""
				,
				null
			},
			{
				"""
				col_date1 = current_date;
				"""
				,
				"""
				col_date1
				"""
				,
				"""
				current_date
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("select insert_vardata()");
						assertQueryAsStr(
							"select col_date1 from vardata",
							List.of(List.of(Date.valueOf(LocalDate.now()).toString())),
							"Must be today"
						);
					}
				}
			},
			{
				"""
				col_int1 = 2+3;
				"""
				,
				"""
				col_int1
				"""
				,
				"""
				(2+3)
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("select insert_vardata()");
						assertQueryAsStr(
							"select col_int1 from vardata",
							List.of(List.of(5)),
							"2+3 = 5"
						);
					}
				}
			},
		};
	}

	@Test(dataProvider = TEST_PROVIDER_NAME)
	public void test(String preconditions, String cols, String values, Runnable tests)
	{
		this.generateAndCheck(
			new ByteArrayInputStream(String.format(CONTRACT, preconditions).getBytes()),
			String.format(EXPECTED_OUTPUT, cols, values)
		);
		if (tests != null) tests.run();
	}
}
