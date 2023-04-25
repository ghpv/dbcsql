package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteSchemaTables extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_myschema_test
	{
		p_id;
	}
	preconditions
	{
		exists myschema_test a (id=p_id);
		exists myschema_test_status status (name = 'active');
		connection between a and status;
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_myschema_test
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'myschema', 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			myschema.test as a
		USING
			myschema.test_status as status
		WHERE
		(
			a.id = p_id
			AND status.name = 'active'
			AND a.test_status_id = status.test_status_id
		)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into myschema.test (id, test_status_id) values (1, 2), (2, 2)")
			.exec("select forget_myschema_test(2::smallint)")
			.assertQuery(
				"select id from myschema.test",
				List.of(List.of(1)),
				"2 must have been deleted"
			)
		;
	}
}
