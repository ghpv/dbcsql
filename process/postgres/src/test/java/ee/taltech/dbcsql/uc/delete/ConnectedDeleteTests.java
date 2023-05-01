package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class ConnectedDeleteTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_all_inactive
	{
	}
	preconditions
	{
		exists test a;
		exists test_status st (name='active');
		connection between a and st;
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_all_inactive
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test AS a
		USING
			public.test_status AS st
		WHERE
			(
				st.name = 'active'
				AND a.test_status_id = st.test_status_id
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test values (1, 'wow', 1), (2, 'not so wow', 2), (3, 'even less wow', 2)")
			.exec("select forget_all_inactive()")
			.assertQuery(
				"select * from test",
				List.of(List.of(1, "wow", 1)),
				"2 must have been deleted"
			)
		;
	}
}
