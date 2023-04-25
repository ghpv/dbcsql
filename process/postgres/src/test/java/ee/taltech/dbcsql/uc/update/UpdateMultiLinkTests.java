package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateMultiLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test_multi
	{
		p_test_id1;
		p_test_id2;
	}
	preconditions
	{
		exists test a(id = p_test_id1);
		exists test b(id = p_test_id2);
		exists test_multi m;
	}
	postconditions
	{
		updated m
		{
			link with a through fk_test1;
			link with b through fk_test2;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test_multi
	(
		p_test_id1 SMALLINT,
		p_test_id2 SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test_multi as m
		SET
			test_id1 = a.id,
			test_id2 = b.id
		FROM
			public.test as a,
			public.test as b
		WHERE
			(
				a.id = p_test_id1
				AND b.id = p_test_id2
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id) values (1), (2), (3)")
			.exec("insert into test_multi (id, test_id1, test_id2) values (1, 1, 1)")
			.exec("select update_test_multi(2::smallint, 3::smallint)")
			.assertQuery(
				"select id, test_id1, test_id2 from test_multi",
				List.of(List.of(1, 2, 3)),
				"Test multi was updated accordingly"
			)
		;
	}
}
