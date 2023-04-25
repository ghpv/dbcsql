package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateRestrictionUnlinkTests extends UseCaseTest
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
		exists test_status st(name = 'active');
	}
	postconditions
	{
		updated a
		{
			unlink from st;
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
		UPDATE
			public.test as a
		SET
			test_status_id = null
		FROM
			public.test_status as st
		WHERE
			(
				a.id = p_id
				AND st.name = 'active'
			)
		;
		UPDATE
			public.test as a
		SET
			data = p_data
		FROM
			public.test_status as st
		WHERE
			(
				a.test_status_id is null
				AND a.id = p_id
				AND st.name = 'active'
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test (id, data, test_status_id) values (1, 'wow', 1), (2, 'wowow', 1);")
			.exec("select op(2::smallint, 'nice')")
			.assertQuery(
				"select id, data, test_status_id from test order by id",
				List.of(List.of(1, "wow", 1), Arrays.asList(new Object[]{2, "nice", null})),
				"Data was updated as requested"
			)
		;
	}
}
