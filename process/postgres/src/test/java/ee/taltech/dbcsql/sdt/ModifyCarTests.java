package ee.taltech.dbcsql.sdt;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class ModifyCarTests extends SampleDataTest
{
	private static final String CONTRACT = "06_modify_car.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION change_car
	(
		p_car_id_old INTEGER,
		p_car_id_new INTEGER,
		p_name VARCHAR,
		p_model VARCHAR,
		p_release_year SMALLINT,
		p_reg_number VARCHAR,
		p_seat_count SMALLINT,
		p_engine_volume NUMERIC,
		p_vin_code VARCHAR,
		p_fuel_type_id SMALLINT,
		p_brand_id SMALLINT
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		UPDATE
			auto as a
		SET
			auto_kood = p_car_id_new,
			nimetus = p_name,
			mudel = p_model,
			valjalaske_aasta = p_release_year,
			reg_number = p_reg_number,
			istekohtade_arv = p_seat_count,
			mootori_maht = p_engine_volume,
			vin_kood = p_vin_code,
			auto_kytuse_liik_kood = akl.auto_kytuse_liik_kood,
			auto_mark_kood = am.auto_mark_kood
		FROM
			auto_seisundi_liik as status,
			auto_kytuse_liik as akl,
			auto_mark as am
		WHERE
			(
				status.nimetus in ('Ootel', 'Mitteaktiivne')
				AND a.auto_kood = p_car_id_old
				AND a.auto_seisundi_liik_kood = status.auto_seisundi_liik_kood
				AND akl.auto_kytuse_liik_kood = p_fuel_type_id
				AND am.auto_mark_kood = p_brand_id
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

		List<Object> values = Arrays.asList(
			1,
			1,
			"new name",
			"new model",
			(short) 2022,
			"00000001",
			(short) 5,
			3.4,
			"0123456789A",
			(short) 1,
			(short) 1
		);

		List<String> cols = Arrays.asList(
			"auto_kood",
			"auto_kood",
			"nimetus",
			"mudel",
			"valjalaske_aasta",
			"reg_number",
			"istekohtade_arv",
			"mootori_maht",
			"vin_kood",
			"auto_kytuse_liik_kood",
			"auto_mark_kood"
		);

		this.sdb.addCar(1);

		this
			.exec("select change_car (?, ?, ?, ?, ?, ?, ?, ?::numeric, ?, ?, ?)", values.toArray())
			.assertQueryAsStr(
				"select " + String.join(",", cols) + " from auto",
				List.of(values),
				"Must have new car inserted"
			)
		;
	}
}
