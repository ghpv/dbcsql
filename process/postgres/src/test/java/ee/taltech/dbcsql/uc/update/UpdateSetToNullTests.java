package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateSetToNullTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test
	{
		p_id;
	}
	preconditions
	{
		exists test a(id = p_id);
	}
	postconditions
	{
		updated a
		{
			data = null;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test as a
		SET
			data = null
		WHERE
			a.id = p_id
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, data) values (1, 'not wow')")
			.exec("select update_test(1::smallint)")
			.assertQuery(
				"select data from test",
				List.of(Arrays.asList(new Object[]{null})),
				"Data was updated"
			)
		;
	}
}
