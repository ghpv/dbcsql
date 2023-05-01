package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertWithLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_test_with_status
	{
		p_id;
	}
	preconditions
	{
		exists test_status status(name='waiting');
	}
	postconditions
	{
		inserted into test a
		{
			id = p_id;
			link with status;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_test_with_status
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.test AS a
		(
			id,
			test_status_id
		)
		SELECT
			p_id,
			status.test_status_id
		FROM
			public.test_status AS status
		WHERE
			(
				status.name = 'waiting'
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("select add_test_with_status(1::smallint)")
			.assertQuery(
				"select id, test_status_id from test",
				List.of(List.of(1, 1)),
				"Data was added"
			)
		;
	}
}
