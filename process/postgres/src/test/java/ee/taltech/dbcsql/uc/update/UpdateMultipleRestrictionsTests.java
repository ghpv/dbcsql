package ee.taltech.dbcsql.uc.update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.uc.UseCaseTest;

public class UpdateMultipleRestrictionsTests extends UseCaseTest
{
	//Note: please don't actually do this, just combine all updates into single postcond
	private static final InputStream CONTRACT_STREAM = new ByteArrayInputStream("""
	operation op
	{
		p_col_text1;
		p_col_int1;
		p_col_int2;
		p_col_timestamp1;
		p_col_date1;
		p_col_short1;
		p_col_float1;
	}
	preconditions
	{
		exists vardata vd(col_int1 = p_col_int1);
	}
	postconditions
	{
		updated vd
		{
			col_text1 = p_col_text1;
		};
		updated vd
		{
			col_int2 = p_col_int2;
		};
		updated vd
		{
			col_timestamp1 = p_col_timestamp1;
		};
		updated vd
		{
			col_date1 = p_col_date1;
		};
		updated vd
		{
			col_short1 = p_col_short1;
		};
		updated vd
		{
			col_float1 = p_col_float1;
		};
	}
	""".getBytes());

	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION op
	(
		p_col_text1 VARCHAR,
		p_col_int1 INTEGER,
		p_col_int2 INTEGER,
		p_col_timestamp1 TIMESTAMP,
		p_col_date1 DATE,
		p_col_short1 SMALLINT,
		p_col_float1 NUMERIC
	)
	RETURNS VOID
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			public.vardata AS vd
		SET
			col_text1 = p_col_text1
		WHERE
			vd.col_int1 = p_col_int1
		;
		UPDATE
			public.vardata AS vd
		SET
			col_int2 = p_col_int2
		WHERE
			(
				vd.col_text1 = p_col_text1
				AND vd.col_int1 = p_col_int1
			)
		;
		UPDATE
			public.vardata AS vd
		SET
			col_timestamp1 = p_col_timestamp1
		WHERE
			(
				vd.col_text1 = p_col_text1
				AND vd.col_int2 = p_col_int2
				AND vd.col_int1 = p_col_int1
			)
		;
		UPDATE
			public.vardata AS vd
		SET
			col_date1 = p_col_date1
		WHERE
			(
				vd.col_timestamp1 = p_col_timestamp1
				AND vd.col_text1 = p_col_text1
				AND vd.col_int2 = p_col_int2
				AND vd.col_int1 = p_col_int1
			)
		;
		UPDATE
			public.vardata AS vd
		SET
			col_short1 = p_col_short1
		WHERE
			(
				vd.col_date1 = p_col_date1
				AND vd.col_timestamp1 = p_col_timestamp1
				AND vd.col_text1 = p_col_text1
				AND vd.col_int2 = p_col_int2
				AND vd.col_int1 = p_col_int1
			)
		;
		UPDATE
			public.vardata AS vd
		SET
			col_float1 = p_col_float1
		WHERE
			(
				vd.col_date1 = p_col_date1
				AND vd.col_timestamp1 = p_col_timestamp1
				AND vd.col_text1 = p_col_text1
				AND vd.col_int2 = p_col_int2
				AND vd.col_short1 = p_col_short1
				AND vd.col_int1 = p_col_int1
			)
		;
	END;
	""";

	@Test
	public void test()
	{
		this.generateAndCheck(CONTRACT_STREAM, EXPECTED_OUTPUT);

		this
			.exec("insert into vardata (col_int1) values (1), (2)")
			.exec("select op('wow', 1, 50, '2020-01-01T00:00:00', '2020-01-01', 5::smallint, 5.3)")
			.assertQueryAsStr(
				"select col_text1, col_int1, col_int2, col_timestamp1, col_date1, col_short1, col_float1 from vardata order by col_int1",
				List.of(
					List.of("wow", "1", "50", "2020-01-01 00:00:00.0", "2020-01-01", "5", "5.300"),
					Arrays.asList(new Object[]{null, 2, null, null, null, null, null})
				),
				"Value must update correctly"
			)
	
		;
	}
}
