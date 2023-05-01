package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateLongMultiLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_test_multi2
	{
		p_test_id;
	}
	preconditions
	{
		exists test a(id = p_test_id);
		exists test_multi2 m;
	}
	postconditions
	{
		updated m
		{
			link with a through fk_test2, fk_test1;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_test_multi2
	(
		p_test_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.test_multi2 AS m
		SET
			test_multi_id2 = test_multi.id
		FROM
			public.test AS a,
			public.test_multi AS test_multi
		WHERE
			(
				a.id = p_test_id
				AND a.id = test_multi.test_id1
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id) values (1), (2)")
			.exec("insert into test_multi (id, test_id1, test_id2) values (3, 1, 2), (4, 2, 1)")
			.exec("insert into test_multi2 (id, test_multi_id1, test_multi_id2) values (5, 4, 4)")
			.exec("select update_test_multi2(1::smallint)")
			.assertQuery(
				"select test_multi_id1, test_multi_id2 from test_multi2",
				List.of(Arrays.asList(4, 3)),
				"test_multi2 was updated correctly"
			)
		;
	}
}
