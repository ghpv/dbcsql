package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteMultipleConnectionsLongTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test_multi_id2
	{
		p_id;
	}
	preconditions
	{
		exists test_multi b;
		exists test_multi2 c;
		exists test a(id = p_id);
		connection between a and b through fk_test1;
		connection between b and c through fk_test2;
	}
	postconditions
	{
		deleted c;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_test_multi_id2
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test_multi2 as c
		USING
			public.test as a,
			public.test_multi as b
		WHERE
			(
				a.id = p_id
				AND a.id = b.test_id1
				AND b.id = c.test_multi_id2
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);
	}
}
