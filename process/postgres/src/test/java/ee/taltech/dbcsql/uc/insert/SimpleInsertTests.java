package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class SimpleInsertTests extends UseCaseTest
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
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_test
	(
		p_id SMALLINT,
		p_data VARCHAR
	)
	RETURNS VOID
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
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("select add_test(1::smallint, 'wow')")
			.assertQuery(
				"select id, data from test",
				List.of(List.of(1, "wow")),
				"Data was added"
			)
		;
	}
}
