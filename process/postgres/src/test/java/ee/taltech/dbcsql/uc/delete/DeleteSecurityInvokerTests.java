package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteSecurityInvokerTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_test
	{
		p_id;
	}
	preconditions
	{
		exists test a (id=p_id);
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
	LANGUAGE SQL SECURITY INVOKER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test AS a
		WHERE
			a.id = p_id
		;
	END;
	""";

	@Override
	protected void extraOperationsOnContext(GenerationContextSubBuilder contextBuilder)
	{
		contextBuilder.withSecurityInvoker(true);
	}

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test values (1), (2)")
			.exec("select forget_test(2::smallint)")
			.assertQuery(
				"select id from test",
				List.of(List.of(1)),
				"2nd must be removed"
			)
		;
		;
	}
}
