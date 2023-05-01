package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateRelinkWithUnrelatedTests extends UseCaseTest
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
		exists vardata vd(col_int1 = 5);
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
			public.test AS a
		SET
			test_status_id = new_status.test_status_id
		FROM
			public.test_status AS old_status,
			public.test_status AS new_status,
			public.vardata AS vd
		WHERE
			(
				a.id = p_id
				AND old_status.name = 'waiting'
				AND new_status.name = 'active'
				AND a.test_status_id = old_status.test_status_id
				AND vd.col_int1 = 5
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
				List.of(List.of(1, 1)),
				"Status must not update because vardata is empty"
			)
			.exec("insert into vardata (col_int1) values (5)")
			.exec("select activate_test(1::smallint)")
			.assertQuery(
				"select id, test_status_id from test",
				List.of(List.of(1, 2)),
				"Status must change now"
			)
		;
	}
}
