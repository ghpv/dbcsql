package ee.taltech.dbcsql.uc.insert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class InsertRestrictionsNotApplyTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation op
	{
		p_id;
		p_data;
	}
	preconditions
	{
		exists test a(id = p_id);
	}
	postconditions
	{
		updated a
		{
			data = p_data;
		};
		inserted into test b
		{
			id = p_id + 1;
			data = 'really ' || p_data;
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
		UPDATE
			public.test as a
		SET
			data = p_data
		WHERE
			a.id = p_id
		;
		INSERT INTO
			public.test as b
		(
			id,
			data
		)
		SELECT
			(p_id+1),
			('really '||p_data)
		FROM
			public.test as a
		WHERE
			(
				a.data = p_data
				AND a.id = p_id
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, data) values (1, 'wow'), (2, 'wowow');")
			.exec("select op(2::smallint, 'nice')")
			.assertQuery(
				"select id, data from test order by id",
				List.of(List.of(1, "wow"), List.of(2, "nice"), List.of(3, "really nice")),
				"Data was added as requested"
			)
		;
	}
}
