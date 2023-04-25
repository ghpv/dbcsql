package ee.taltech.dbcsql.uc.delete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class DeleteSpacedTableNameWithContextTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation forget_annoying_name
	{
		p_id;
	}
	preconditions
	{
		exists aname a (id=p_id);
	}
	postconditions
	{
		deleted a;
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_annoying_name
	(
		p_id SMALLINT
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			"Annoying name" as a
		WHERE
			a.id = p_id
		;
	END;
	""";

	private static final ByteArrayInputStream CONTEXT_STREAM = new ByteArrayInputStream("""
	table aname="Annoying name"
	{
		id SMALLINT;
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
			.exec("insert into \"Annoying name\" values (1), (2)")
			.exec("select forget_annoying_name(2::smallint)")
			.assertQuery(
				"select id from \"Annoying name\"",
				List.of(List.of(1)),
				"2nd must be removed"
			)
		;
		;
	}
}
