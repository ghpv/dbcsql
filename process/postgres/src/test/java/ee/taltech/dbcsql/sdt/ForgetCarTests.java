package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class ForgetCarTests extends SampleDataTest
{
	private static final String CONTRACT = "02_forget_car.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION forget_car
	(
		p_car_id INTEGER
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			auto AS c
		USING
			auto_seisundi_liik AS status
		WHERE
			(
				c.auto_kood = p_car_id
				AND status.nimetus = 'Ootel'
				AND c.auto_seisundi_liik_kood = status.auto_seisundi_liik_kood
			)
		RETURNING
			c.auto_kood
		;
	END;
	""";

	@Test
	public void testExecution()
	{
		this.generateAndCheck(CONTRACT, EXPECTED_OUTPUT);

		this.sdb
			.addCar(1)
			.addCar(2)
			.setCarStatusWaiting(1)
			.setCarStatusActive(2)
		;
		this
			.assertQuery(
				"select auto_kood from auto",
				List.of(List.of(1), List.of(2)),
				"Initially there are 2 cars"
			)
			.exec("select forget_car(1)")
			.assertQuery(
				"select auto_kood from auto",
				List.of(List.of(2)),
				"Car 1 must be deleted"
			)
			.exec("select forget_car(2)")
			.assertQuery(
				"select auto_kood from auto",
				List.of(List.of(2)),
				"Car 2 must not be deleted as it's in incorrect status"
			)
		;
	}
}
