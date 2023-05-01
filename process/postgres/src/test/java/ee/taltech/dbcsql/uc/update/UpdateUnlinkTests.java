package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateUnlinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation unlink_test
	{
		p_id;
	}
	preconditions
	{
		exists test a(id = p_id);
		exists test_status status(name = 'active');
	}
	postconditions
	{
		updated a
		{
			unlink from status;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION unlink_test
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test AS a
		SET
			test_status_id = null
		FROM
			public.test_status AS status
		WHERE
			(
				a.id = p_id
				AND status.name = 'active'
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
			.exec("select unlink_test(1::smallint)")
			.assertQuery(
				"select id, test_status_id from test",
				List.of(Arrays.asList(new Object[]{1, null})),
				"Status must have updated"
			)
		;
	}
}
