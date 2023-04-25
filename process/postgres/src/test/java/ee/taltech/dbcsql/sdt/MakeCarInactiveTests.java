package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class MakeCarInactiveTests extends SampleDataTest
{
	private static final String CONTRACT = "04_make_car_inactive.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION make_car_inactive
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
			auto_seisundi_liik as new_status,
			auto_seisundi_liik as old_status
		WHERE
			(
				new_status.nimetus in ('Mitteaktiivne')
				AND old_status.nimetus in ('Aktiivne')
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
			.exec("select make_car_inactive (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 3)),
				"Car is now inactive"
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
			.exec("select make_car_inactive (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Car is still waiting"
			)
		;
	}
}
