package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertWithMultiLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_to_test_multi
	{
		p_test_id1;
		p_test_id2;
	}
	preconditions
	{
		exists test a(id = p_test_id1);
		exists test b(id = p_test_id2);
	}
	postconditions
	{
		inserted into test_multi m
		{
			link with a through fk_test1;
			link with b through fk_test2;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_to_test_multi
	(
		p_test_id1 SMALLINT,
		p_test_id2 SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.test_multi as m
		(
			test_id1,
			test_id2
		)
		SELECT
			a.id,
			b.id
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
			.exec("insert into test (id) values (1), (2)")
			.exec("select add_to_test_multi(2::smallint, 1::smallint)")
			.assertQuery(
				"select test_id1, test_id2 from test_multi",
				List.of(List.of(2, 1)),
				"test_multi must link test_id1 as 2 and test_id2 as 1"
			)
		;
	}
}
