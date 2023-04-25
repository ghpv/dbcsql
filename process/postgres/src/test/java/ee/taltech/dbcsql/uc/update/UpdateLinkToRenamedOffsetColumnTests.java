package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.InputPhase.GenerationContextSubBuilder;
import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateLinkToRenamedOffsetColumnTests extends UseCaseTest
{
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation update_vardata
	{
		p_int1;
	}
	preconditions
	{
		exists vardata a(ci1 = p_int1);
	}
	postconditions
	{
		updated a
		{
			ci2 = ci1 + 1;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION update_vardata
	(
		p_int1 INTEGER
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			vardata as a
		SET
			col_int2 = (a.col_int1+1)
		WHERE
			a.col_int1 = p_int1
		;
	END;
	""";

	private static final ByteArrayInputStream CONTEXT_STREAM = new ByteArrayInputStream("""
	table vardata
	{
		ci1 = col_int1 INTEGER;
		ci2 = col_int2 INTEGER;
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
			.exec("insert into vardata (col_int1, col_int2) values (1, 0), (2, 0)")
			.exec("select update_vardata(1)")
			.assertQuery(
				"select col_int1, col_int2 from vardata order by col_int1",
				List.of(List.of(1, 2), List.of(2, 0)),
				"Only first must have col_int2 set to col_int1 + 1"
			)
		;
	}
}
