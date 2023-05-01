package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_status
	{
		p_id;
		p_status_name;
	}
	preconditions
	{
		exists test_status status(name=p_status_name);
		exists test a(id = p_id);
	}
	postconditions
	{
		updated a
		{
			link with status;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_status
	(
		p_id SMALLINT,
		p_status_name TEXT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test AS a
		SET
			test_status_id = status.test_status_id
		FROM
			public.test_status AS status
		WHERE
			(
				status.name = p_status_name
				AND a.id = p_id
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
			.exec("select update_status(1::smallint, 'active')")
			.assertQuery(
				"select id, test_status_id from test",
				List.of(List.of(1, 2)),
				"Status was updated"
			)
		;
	}
}
