package ee.taltech.dbcsql.sdt;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class RegisterCarTests extends SampleDataTest
{
	private static final String CONTRACT = "01_register_car.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION register_car
	(
		p_car_id INTEGER,
		p_name VARCHAR,
		p_model VARCHAR,
		p_release_year SMALLINT,
		p_reg_number VARCHAR,
		p_seat_count SMALLINT,
		p_engine_volume NUMERIC,
		p_vin_code VARCHAR,
		p_registrator_id BIGINT,
		p_car_fuel_type_id SMALLINT,
		p_car_brand_id SMALLINT
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			auto as a
		(
			auto_kood,
			nimetus,
			mudel,
			valjalaske_aasta,
			reg_number,
			istekohtade_arv,
			mootori_maht,
			vin_kood,
			auto_seisundi_liik_kood,
			auto_kytuse_liik_kood,
			auto_mark_kood,
			registreerija_id
		)
		SELECT
			p_car_id,
			p_name,
			p_model,
			p_release_year,
			p_reg_number,
			p_seat_count,
			p_engine_volume,
			p_vin_code,
			status.auto_seisundi_liik_kood,
			cft.auto_kytuse_liik_kood,
			cb.auto_mark_kood,
			w.isik_id
		FROM
			auto_seisundi_liik as status,
			auto_kytuse_liik as cft,
			auto_mark as cb,
			tootaja as w
		WHERE
			(
				status.nimetus = 'Ootel'
				AND cft.auto_kytuse_liik_kood = p_car_fuel_type_id
				AND cb.auto_mark_kood = p_car_brand_id
				AND w.isik_id = p_registrator_id
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
			"name",
			"model",
			(short) 2021,
			"00000000",
			(short) 4,
			4.3,
			"0123456789A",
			(long) 1,
			(short) 1,
			(short) 1
		);

		List<String> cols = Arrays.asList(
			"auto_kood",
			"nimetus",
			"mudel",
			"valjalaske_aasta",
			"reg_number",
			"istekohtade_arv",
			"mootori_maht",
			"vin_kood",
			"registreerija_id",
			"auto_kytuse_liik_kood",
			"auto_mark_kood"
		);

		this
			.assertQuery(
				"select " + String.join(",", cols) + " from auto",
				List.of(),
				"Start empty"
			)
			.exec("select register_car (?, ?, ?, ?, ?, ?, ?::numeric, ?, ?, ?, ?)", values.toArray())
			.assertQueryAsStr(
				"select " + String.join(",", cols) + " from auto",
				List.of(values),
				"Must have new car inserted"
			)
		;
	}
}
