package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateRelinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation activate_test
	{
		p_id;
	}
	preconditions
	{
		exists test a(id = p_id);
		exists test_status old_status(name = 'waiting');
		exists test_status new_status(name = 'active');
		connection between a and old_status;
	}
	postconditions
	{
		updated a
		{
			link with new_status;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION activate_test
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
			test_status_id = new_status.test_status_id
		FROM
			public.test_status as old_status,
			public.test_status as new_status
		WHERE
			(
				a.id = p_id
				AND old_status.name = 'waiting'
				AND new_status.name = 'active'
				AND a.test_status_id = old_status.test_status_id
			)

		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, test_status_id) values (1, 1)")
			.exec("select activate_test(1::smallint)")
			.assertQuery(
				"select id, test_status_id from test",
				List.of(List.of(1, 2)),
				"Status must have updated"
			)
		;
	}
}
