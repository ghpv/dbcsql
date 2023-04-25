package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteCheckUnrelatedTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test
	{
		p_id;
	}
	preconditions
	{
		exists test a (id=p_id);
		exists vardata vd(col_int1 = 5);
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_test
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test as a
		USING
			public.vardata as vd
		WHERE
			(
				a.id = p_id
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
			.exec("insert into test values (1), (2)")
			.exec("select forget_test(1::smallint)")
			.assertQuery(
				"select id from test order by id",
				List.of(List.of(1), List.of(2)),
				"Nothing must be deleted because status 'non-existent' does not exist"
			)
			.exec("insert into vardata (col_int1) values (5)")
			.exec("select forget_test(1::smallint)")
			.assertQuery(
				"select id from test order by id",
				List.of(List.of(2)),
				"1 must be deleted because all preconditions pass"
			)
		;
	}
}
