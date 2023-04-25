package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteMultipleConnectionsTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test_multi_id1
	{
		p_id;
	}
	preconditions
	{
		exists test_multi b;
		exists test a(id = p_id);
		connection between a and b through id1;
	}
	postconditions
	{
		deleted b;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_test_multi_id1
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			test_multi as b
		USING
			test as a
		WHERE
			(
				a.id = p_id
				AND a.id = b.test_id1
			)
		;
	END;
	""";

	private static final ByteArrayInputStream CONTEXT_STREAM = new ByteArrayInputStream("""
	table test
	{
		id SMALLINT;
	};
	table test_multi
	{
		id SMALLINT;
		test_id1 SMALLINT;
		test_id2 SMALLINT;
	};
	connection between test and test_multi called id1
	{
		id = test_id1;
	};
	connection between test and test_multi called id2
	{
		id = test_id2;
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
			.exec("insert into test (id) values (1), (2), (3)")
			.exec("insert into test_multi (id, test_id1, test_id2) values (1, 1, 3), (2, 3, 1)")
			.exec("select forget_test_multi_id1(1::smallint)")
			.assertQuery(
				"select id from test_multi",
				List.of(List.of(2)),
				"Only 2 entry must remain, as it's not connected to test through test_id1"
			)
		;
	}
}
