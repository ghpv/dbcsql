package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertCheckUnrelatedTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_test
	{
		p_id;
		p_data;
	}
	preconditions
	{
		exists vardata vd(col_int1 = 5);
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
		SELECT
			p_id,
			p_data
		FROM
			public.vardata AS vd
		WHERE
			vd.col_int1 = 5
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
				List.of(),
				"Nothing was added because such status does not exist"
			)
			.exec("insert into vardata (col_int1) values (5)")
			.exec("select add_test(1::smallint, 'wow')")
			.assertQuery(
				"select id, data from test",
				List.of(List.of(1, "wow")),
				"Data must be inserted now"
			)
		;
	}
}
