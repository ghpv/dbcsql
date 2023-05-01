package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteSchemaWithContextTables extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_myschema_test
	{
		p_id;
	}
	preconditions
	{
		exists mtest a (id=p_id);
		exists mtest_status status (name = 'active');
		connection between a and status;
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_myschema_test
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'myschema', 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			myschema.test AS a
		USING
			myschema.test_status AS status
		WHERE
		(
			a.id = p_id
			AND status.name = 'active'
			AND a.test_status_id = status.test_status_id
		)
		;
	END;
	""";

	private static final ByteArrayInputStream CONTEXT_STREAM = new ByteArrayInputStream("""
	table mtest=myschema.test
	{
		id SMALLINT;
		test_status_id SMALLINT;
	};
	table mtest_status=myschema.test_status
	{
		name VARCHAR;
		test_status_id SMALLINT;
	};
	connection between myschema.test and myschema.test_status
	{
		test_status_id = test_status_id;
	};
	""".getBytes());

	@Override
	protected void loadContext(GenerationContextSubBuilder builder)
	{
		builder.withContextFromStream(CONTEXT_STREAM);
	}

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into myschema.test (id, test_status_id) values (1, 2), (2, 2)")
			.exec("select forget_myschema_test(2::smallint)")
			.assertQuery(
				"select id from myschema.test",
				List.of(List.of(1)),
				"2 must have been deleted"
			)
		;
	}
}
