package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class ChainConnectedNoCascadeDeleteTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_no_cascade_chain
	{
	}
	preconditions
	{
		exists nc_chain_a a data = '1';
		exists nc_chain_b b;
		exists nc_chain_c c;
		exists nc_chain_d d;
		connection between a and b;
		connection between b and c;
		connection between c and d;
	}
	postconditions
	{
		deleted d;
		deleted c;
		deleted b;
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_no_cascade_chain
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.nc_chain_d as d
		USING
			public.nc_chain_a as a,
			public.nc_chain_b as b,
			public.nc_chain_c as c
		WHERE
			(
				a.data = '1'
				AND a.nc_chain_a_id = b.nc_chain_a_id
				AND b.nc_chain_b_id = c.nc_chain_b_id
				AND c.nc_chain_c_id = d.nc_chain_c_id
			)
		;
		DELETE FROM
			public.nc_chain_c as c
		USING
			public.nc_chain_a as a,
			public.nc_chain_b as b
		WHERE
			(
				a.data = '1'
				AND a.nc_chain_a_id = b.nc_chain_a_id
				AND b.nc_chain_b_id = c.nc_chain_b_id
			)
		;
		DELETE FROM
			public.nc_chain_b as b
		USING
			public.nc_chain_a as a
		WHERE
			(
				a.data = '1'
				AND a.nc_chain_a_id = b.nc_chain_a_id
			)
		;
		DELETE FROM
			public.nc_chain_a as a
		WHERE
			a.data = '1'
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into nc_chain_a values (1, '1', '1'), (2, '2', '2')")
			.exec("insert into nc_chain_b values (1, '1', '1', 1), (2, '2', '2', 2)")
			.exec("insert into nc_chain_c values (1, '1', '1', 1), (2, '2', '2', 2)")
			.exec("insert into nc_chain_d values (1, 'test', '1', 1), (2, 'not test', '2', 2)")
			.exec("select forget_no_cascade_chain()")
			.assertQuery(
				"select * from nc_chain_d",
				List.of(List.of(2, "not test", "2", 2)),
				"nc_chain_d 1 was related to nc_chain_c 1"
			)
			.assertQuery(
				"select * from nc_chain_c",
				List.of(List.of(2, "2", "2", 2)),
				"nc_chain_c 1 was related to nc_chain_b 1"
			)
			.assertQuery(
				"select * from nc_chain_b",
				List.of(List.of(2, "2", "2", 2)),
				"nc_chain_b 1 was related to nc_chain_a 1"
			)
			.assertQuery(
				"select * from nc_chain_a",
				List.of(List.of(2, "2", "2")),
				"nc_chain_a must lose 1"
			)
		;
	}
}
