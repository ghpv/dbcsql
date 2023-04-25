package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertRestrictionMustApplyTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation op
	{
		p_id;
		p_data;
	}
	preconditions
	{
	}
	postconditions
	{
		inserted into test a
		{
			id = p_id;
		};
		updated a
		{
			data = p_data;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION op
	(
		p_id SMALLINT,
		p_data VARCHAR
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			public.test as a
		(
			id
		)
		VALUES
		(
			p_id
		)
		;
		UPDATE
			public.test as a
		SET
			data = p_data
		WHERE
			a.id = p_id
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("select op(1::smallint, 'wow')")
			.exec("select op(2::smallint, 'wowow')")
			.assertQuery(
				"select id, data from test order by id",
				List.of(List.of(1, "wow"), List.of(2, "wowow")),
				"Data was added as requested"
			)
		;
	}
}
