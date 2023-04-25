package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteUnicodeStringTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_vardata
	{
	}
	preconditions
	{
		exists vardata a (col_text1 = 'check out my 石橋');
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
			a.col_text1 = 'check out my 石橋'
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into vardata (col_text1) values ('check out my 石橋'), ('愛と罰')")
			.exec("select forget_vardata()")
			.assertQuery(
				"select col_text1 from vardata order by col_text1",
				List.of(List.of("愛と罰")),
				"Only this value must remain"
			)
		;
	}
}
