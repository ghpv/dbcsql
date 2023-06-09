package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertReturningTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_test
	{
		p_id;
		p_data;
	}
	preconditions
	{
	}
	postconditions
	{
		inserted into test a
		{
			id = p_id;
			data = p_data;
		}
		return 0;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_test
	(
		p_id SMALLINT,
		p_data VARCHAR
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.test AS a
		(
			id,
			data
		)
		VALUES
		(
			p_id,
			p_data
		)
		RETURNING
			0
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.assertQuery(
				"select add_test(1::smallint, 'wow')",
				List.of(List.of(0)),
				"Return value present"
			)
			.assertQuery(
				"select id, data from test",
				List.of(List.of(1, "wow")),
				"Data was added"
			)
		;
	}
}
