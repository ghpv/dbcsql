package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteBadNamingInRestrictionTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation "forget test"
	{
		"id spaced";
	}
	preconditions
	{
		exists test a (id = "id spaced");
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION f_forget_test
	(
		p_id_spaced SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			public.test as a
		WHERE
			a.id = p_id_spaced
		;
	END;
	""";

	@Override
	protected void extraOperationsOnContext(GenerationContextSubBuilder contextBuilder)
	{
		contextBuilder.withFunctionPrefix("f");
		contextBuilder.withArgumentPrefix("p");
	}

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into test values (1), (2)")
			.exec("select f_forget_test(2::smallint)")
			.assertQuery(
				"select id from test",
				List.of(List.of(1)),
				"2 must have been deleted"
			)
		;
	}
}
