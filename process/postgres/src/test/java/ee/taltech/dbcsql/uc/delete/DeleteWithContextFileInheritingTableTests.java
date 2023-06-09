package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteWithContextFileInheritingTableTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_all_inactive
	{
	}
	preconditions
	{
		exists test a;
		exists test_status b (name='active');
		connection between a and b;
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_all_inactive
	(
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			test AS a
		USING
			test_status AS b
		WHERE
			(
				b.name = 'active'
				AND a.test_status_id = b.test_status_id
			)
		;
	END;
	""";

	private static final ByteArrayInputStream CONTEXT_STREAM = new ByteArrayInputStream("""
	table test
	{
		test_status_id SMALLINT;
	};
	table generic_status
	{
		name VARCHAR;
		test_status_id SMALLINT;
	};
	table test_status extends generic_status
	{
	};
	connection between test and test_status
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
			.exec("insert into test values (1, 'wow', 1), (2, 'not so wow', 2), (3, 'even less wow', 2)")
			.exec("select forget_all_inactive()")
			.assertQuery(
				"select * from test",
				List.of(List.of(1, "wow", 1)),
				"2 must have been deleted"
			)
		;
	}
}
