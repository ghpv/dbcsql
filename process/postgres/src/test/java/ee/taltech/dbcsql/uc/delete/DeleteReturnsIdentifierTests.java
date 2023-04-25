package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteReturnsIdentifierTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test
	{
		p_id;
	}
	preconditions
	{
		exists test a (id=p_id);
	}
	postconditions
	{
		deleted a return _identifier;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_test
	(
		p_id SMALLINT
	)
	RETURNS SMALLINT
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test as a
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
			.exec("insert into test values (1), (2)")
			.assertQuery(
				"select forget_test(2::smallint)",
				List.of(List.of(2)),
				"ID of removed element must be returned"
			)
			.assertQuery(
				"select id from test",
				List.of(List.of(1)),
				"2nd must be removed"
			)
			.assertQuery(
				"select forget_test(2::smallint)",
				List.of(Arrays.asList(new Object[]{null})),
				"No return since function was not called"
			)
		;
		;
	}
}
