package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateReturningTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test
	{
		p_id;
		p_data;
	}
	preconditions
	{
		exists test a(id = p_id);
	}
	postconditions
	{
		updated a
		{
			data = p_data;
		}
		return id;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test
	(
		p_id SMALLINT,
		p_data VARCHAR
	)
	RETURNS SMALLINT
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test AS a
		SET
			data = p_data
		WHERE
			a.id = p_id
		RETURNING
			a.id
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, data) values (1, 'not wow')")
			.assertQuery(
				"select update_test(1::smallint, 'wow')",
				List.of(List.of(1)),
				"Correct ID was updated"
			)
			.assertQuery(
				"select data from test",
				List.of(List.of("wow")),
				"Data was updated"
			)
			.assertQuery(
				"select update_test(2::smallint, 'wow')",
				List.of(Arrays.asList(new Object[]{null})),
				"No data, so return null"
			)
		;
	}
}
