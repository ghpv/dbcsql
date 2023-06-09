package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class SimpleUpdateTargetTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test
	{
		p_id;
		p_data;
	}
	preconditions
	{
		exists test a(id = p_id);
	}
	postconditions
	{
		updated a
		{
			data = p_data;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test
	(
		p_id SMALLINT,
		p_data VARCHAR
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test AS a
		SET
			data = p_data
		WHERE
			a.id = p_id
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, data) values (1, 'not wow')")
			.exec("select update_test(1::smallint, 'wow')")
			.assertQuery(
				"select id, data from test",
				List.of(List.of(1, "wow")),
				"Data was updated"
			)
		;
	}
}
