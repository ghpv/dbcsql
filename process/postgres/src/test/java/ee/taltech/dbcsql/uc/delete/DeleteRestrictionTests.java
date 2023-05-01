package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteRestrictionTests extends UseCaseTest
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
				exists vardata a (col_int1=5 or (col_text1 != 'wow' and col_float1 > 3.4));
				"""
				,
				"""
				(
					a.col_int1 = 5
					OR
					(
						a.col_text1 <> 'wow'
						AND a.col_float1 > 3.4
					)
				)
				"""
				,
				null
			},
			{
				"""
				exists vardata a (col_array1 = '{{5}, {6}}');
				"""
				,
				"""
				a.col_array1 = '{{5}, {6}}'
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("insert into vardata (col_array1) values ('{{5}, {6}}'), ('{{5, 6}}')");
						exec("select from forget_vardata()");
						assertQuery(
							"select count(col_array1) from vardata",
							List.of(List.of((long) 1)),
							"Only 1 row must remain"
						);
					}
				}
			},
			{
				"""
				exists vardata a (col_short1 = 5);
				"""
				,
				"""
				a.col_short1 = 5
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("insert into vardata (col_short1) values (5), (6)");
						exec("select from forget_vardata()");
						assertQuery(
							"select col_short1 from vardata",
							List.of(List.of(6)),
							"1 must be removed"
						);
					}
				}
			},
			{
				"""
				exists vardata a (col_int1 not in (5, 6));
				"""
				,
				"""
				a.col_int1 not in (5, 6)
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("insert into vardata (col_int1) values (5), (6), (3)");
						exec("select from forget_vardata()");
						assertQuery(
							"select col_int1 from vardata",
							List.of(List.of(5), List.of(6)),
							"3 must be removed as it's not in (5, 6)"
						);
					}
				}
			},
			{
				"""
				exists vardata a (col_int1 is not null);
				"""
				,
				"""
				a.col_int1 is not null
				"""
				,
				new Runnable()
				{
					@Override
					public void run()
					{
						exec("insert into vardata (col_int1) values (5), (null)");
						exec("select from forget_vardata()");
						assertQuery(
							"select col_int1 from vardata",
							List.of(Arrays.asList(new Object[]{null})),
							"non null values must be removed"
						);
					}
				}
			},
			{
				"""
				exists vardata a (col_date1 > current_date and col_timestamp1 > current_timestamp);
				"""
				,
				"""
				(
					a.col_date1 > current_date
					AND a.col_timestamp1 > current_timestamp
				)
				"""
				,
				null
			},
			{
				"""
				exists vardata a (col_int1 = 5 + 5 * 3 / 4 - 6);
				"""
				,
				"""
				a.col_int1 = ((5+((5*3)/4))-6)
				"""
				,
				null
			},
			{
				"""
				exists vardata a (col_int1 = ((5 + 5) * (3)) / (4 - 6));
				"""
				,
				"""
				a.col_int1 = (((5+5)*3)/(4-6))
				"""
				,
				null
			},
			{
				"""
				exists vardata a (col_text1 = 'a' || 'b');
				"""
				,
				"""
				a.col_text1 = ('a'||'b')
				"""
				,
				null
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
