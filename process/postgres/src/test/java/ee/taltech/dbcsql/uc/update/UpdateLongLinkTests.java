package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateLongLinkTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_chain_d
	{
		p_chain_d_id;
		p_chain_a_id;
	}
	preconditions
	{
		exists chain_a a(chain_a_id = p_chain_a_id);
		exists chain_d d(chain_d_id = p_chain_d_id);
	}
	postconditions
	{
		updated d
		{
			link with a;
		};
	};
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_chain_d
	(
		p_chain_d_id INTEGER,
		p_chain_a_id INTEGER
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.chain_d as d
		SET
			chain_c_id = chain_c.chain_c_id
		FROM
			public.chain_a as a,
			public.chain_b as chain_b,
			public.chain_c as chain_c
		WHERE
			(
				a.chain_a_id = p_chain_a_id
				AND d.chain_d_id = p_chain_d_id
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
			.exec("insert into chain_d values (7, '7', '7', 5), (8, '8', '8', 6)")
			.exec("select update_chain_d(7, 2)")
			.assertQuery(
				"select chain_d_id, chain_c_id from chain_d order by chain_d_id",
				List.of(List.of(7, 6), List.of(8, 6)),
				"Chain_d 7 was relinked to 6"
			)
		;
	}
}
