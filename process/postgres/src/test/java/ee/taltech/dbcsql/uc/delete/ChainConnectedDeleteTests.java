package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class ChainConnectedDeleteTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_chain
	{
	}
	preconditions
	{
		exists chain_a a;
		exists chain_d d(data = 'test');
		connection between a and d;
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_chain
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.chain_a as a
		USING
			public.chain_d as d,
			public.chain_b as chain_b,
			public.chain_c as chain_c
		WHERE
			(
				d.data = 'test'
				AND a.chain_a_id = chain_b.chain_a_id
				AND chain_b.chain_b_id = chain_c.chain_b_id
				AND chain_c.chain_c_id = d.chain_c_id
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
			.exec("insert into chain_b values (1, '1', '1', 1), (2, '2', '2', 2)")
			.exec("insert into chain_c values (1, '1', '1', 1), (2, '2', '2', 2)")
			.exec("insert into chain_d values (1, 'test', '1', 1), (2, 'not test', '2', 2)")
			.exec("select forget_chain()")
			.assertQuery(
				"select * from chain_a",
				List.of(List.of(2, "2", "2")),
				"chain_a must lose 1 which is related to chain_d with data = 'test'"
			)
			.assertQuery(
				"select * from chain_b",
				List.of(List.of(2, "2", "2", 2)),
				"chain_b delete cascaded"
			)
			.assertQuery(
				"select * from chain_c",
				List.of(List.of(2, "2", "2", 2)),
				"chain_c delete cascaded"
			)
			.assertQuery(
				"select * from chain_d",
				List.of(List.of(2, "not test", "2", 2)),
				"chain_d delete cascaded"
			)
		;
	}
}
