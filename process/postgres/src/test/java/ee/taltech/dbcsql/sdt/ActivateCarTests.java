package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class ActivateCarTests extends SampleDataTest
{
	private static final String CONTRACT = "03_activate_car.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION activate_car
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
			auto_seisundi_liik as new_status,
			auto_kategooria_omamine as cco
		WHERE
			(
				a.auto_kood = p_car_id
				AND old_status.nimetus in ('Ootel', 'Mitteaktiivne')
				AND new_status.nimetus in ('Aktiivne')
				AND cco.auto_kood = p_car_id
				AND a.auto_kood = cco.auto_kood
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
			.addCar(1);
		;

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Start with a car in waiting status"
			)
			.exec("select activate_car (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Car was not activated as it has no category"
			)
		;

		this.sdb
			.addCarToCategoryTown(1);

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 1)),
				"Start with a car in waiting status"
			)
			.exec("select activate_car (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 2)),
				"Car was activated"
			)
		;

		this.sdb
			.setCarStatusDone(1)
		;

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 4)),
				"Car 1 was moved to done"
			)
			.exec("select activate_car (?)", 1)
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 4)),
				"Car is still done"
			)
		;
	}
}
