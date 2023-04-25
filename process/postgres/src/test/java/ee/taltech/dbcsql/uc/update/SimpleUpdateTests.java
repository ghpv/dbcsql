package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class SimpleUpdateTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test
	{
		p_data;
	}
	preconditions
	{
		exists test a;
	}
	postconditions
	{
		updated a
		{
			data = p_data;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test
	(
		p_data VARCHAR
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test as a
		SET
			data = p_data
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (data) values ('not wow')")
			.exec("select update_test('wow')")
			.assertQuery(
				"select data from test",
				List.of(List.of("wow")),
				"Data was updated"
			)
		;
	}
}
