package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteFloatingTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_vardata
	{
	}
	preconditions
	{
		exists vardata a (col_float1 > 4.);
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_vardata
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.vardata as a
		WHERE
			a.col_float1 > 4.
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into vardata (col_float1) values (5), (-5), (3)")
			.exec("select forget_vardata()")
			.assertQueryAsStr(
				"select col_float1 from vardata order by col_int1",
				List.of(List.of("-5.000"), List.of("3.000")),
				"5 should have gotten deleted"
			)
		;
	}
}
