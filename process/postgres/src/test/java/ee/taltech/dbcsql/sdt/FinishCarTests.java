package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class FinishCarTests extends SampleDataTest
{
	private static final String CONTRACT = "05_finish_car.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION finish_car
	(
		p_car_id INTEGER
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			auto as a
		SET
			auto_seisundi_liik_kood = new_status.auto_seisundi_liik_kood
		FROM
			auto_seisundi_liik as old_status,
			auto_seisundi_liik as new_status
		WHERE
			(
				old_status.nimetus in ('Aktiivne', 'Mitteaktiivne')
				AND new_status.nimetus in ('Lõpetatud')
				AND a.auto_kood = p_car_id
				AND a.auto_seisundi_liik_kood = old_status.auto_seisundi_liik_kood
			)
		RETURNING
			a.auto_kood
		;
	END;
	""";

	@Test
	public void testExecution()
	{
		this.generateAndCheck(CONTRACT, EXPECTED_OUTPUT);

		this.sdb
			.addCar(1)
			.setCarStatusActive(1)
		;

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 2)),
				"Start with a car in active status"
			)
			.exec("select finish_car (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 4)),
				"Car is now done"
			)
		;

		this.sdb
			.setCarStatusWaiting(1)
		;

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Car 1 was moved to waiting"
			)
			.exec("select finish_car (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Car is still waiting"
			)
		;
	}
}
