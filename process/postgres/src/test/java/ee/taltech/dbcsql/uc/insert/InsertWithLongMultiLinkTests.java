package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertWithLongMultiLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_to_test_multi2
	{
		p_test_id;
	}
	preconditions
	{
		exists test a(id = p_test_id);
	}
	postconditions
	{
		inserted into test_multi2 m
		{
			link with a through fk_test2, fk_test1;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_to_test_multi2
	(
		p_test_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.test_multi2 as m
		(
			test_multi_id2
		)
		SELECT
			test_multi.id
		FROM
			public.test as a,
			public.test_multi as test_multi
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
			.exec("insert into test_multi (id, test_id1, test_id2) values (1, 1, 2), (2, 2, 1)")
			.exec("select add_to_test_multi2(1::smallint)")
			.assertQuery(
				"select test_multi_id1, test_multi_id2 from test_multi2",
				List.of(Arrays.asList(new Object[]{null, 1})),
				"Correct test_multi2 is inserted"
			)
		;
	}
}
