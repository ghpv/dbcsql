package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertWithLongLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation add_to_chain_a
	{
		p_chain_a_id;
	}
	preconditions
	{
		exists chain_a a(chain_a_id = p_chain_a_id);
	}
	postconditions
	{
		inserted into chain_d d
		{
			link with a;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_to_chain_a
	(
		p_chain_a_id INTEGER
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.chain_d AS d
		(
			chain_c_id
		)
		SELECT
			chain_c.chain_c_id
		FROM
			public.chain_a AS a,
			public.chain_b AS chain_b,
			public.chain_c AS chain_c
		WHERE
			(
				a.chain_a_id = p_chain_a_id
				AND chain_b.chain_b_id = chain_c.chain_b_id
				AND a.chain_a_id = chain_b.chain_a_id
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into chain_a values (1, '1', '1'), (2, '2', '2')")
			.exec("insert into chain_b values (3, '3', '3', 1), (4, '4', '4', 2)")
			.exec("insert into chain_c values (5, '5', '5', 3), (6, '6', '6', 4)")
			.exec("select add_to_chain_a(1)")
			.assertQuery(
				"select chain_c_id from chain_d",
				List.of(List.of(5)),
				"Chain d was linked to correct chain c"
			)
		;
	}
}
