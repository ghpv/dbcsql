package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteReturningColumnTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test
	{
		p_data;
	}
	preconditions
	{
		exists test a (data=p_data);
	}
	postconditions
	{
		deleted a return id;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_test
	(
		p_data VARCHAR
	)
	RETURNS SMALLINT
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test AS a
		WHERE
			a.data = p_data
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
			.exec("insert into test values (1, 'wow', 1), (2, 'not so wow', 1)")
			.assertQuery(
				"select forget_test('not so wow')",
				List.of(List.of(2)),
				"Must return correct ID"
			)
			.assertQuery(
				"select * from test",
				List.of(List.of(1, "wow", 1)),
				"not so wow must have been deleted"
			)
			.assertQuery(
				"select forget_test('not so wow')",
				List.of(Arrays.asList(new Object[]{null})),
				"No return since function was not called"
			)
		;
	}
}
