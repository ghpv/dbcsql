package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteConcatFuncTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_vardata
	{
		p_data;
	}
	preconditions
	{
		exists vardata a (col_text1 = concat(p_data, 'a' || 'b'));
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_vardata
	(
		p_data VARCHAR
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.vardata as a
		WHERE
			a.col_text1 = concat(p_data,('a'||'b'))
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into vardata (col_text1) values ('a'), ('aab')")
			.exec("select forget_vardata('a')")
			.assertQuery(
				"select col_text1 from vardata",
				List.of(List.of("a")),
				"'a' + 'ab' must be deleted, so 'a' stays"
			)
		;
	}
}
