package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class AddCarToCategoryTests extends SampleDataTest
{
	private static final String CONTRACT = "07_add_car_to_category.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION add_car_to_category
	(
		p_car_id INTEGER,
		p_car_category_id SMALLINT
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		INSERT INTO
			auto_kategooria_omamine as cco
		(
			auto_kood,
			auto_kategooria_kood
		)
		SELECT
			a.auto_kood,
			ci.auto_kategooria_kood
		FROM
			auto as a,
			auto_kategooria as ci,
			auto_seisundi_liik as status
		WHERE
			(
				a.auto_kood = p_car_id
				AND ci.auto_kategooria_kood = p_car_category_id
				AND status.nimetus in ('Aktiivne', 'Mitteaktiivne')
				AND a.auto_seisundi_liik_kood = status.auto_seisundi_liik_kood
			)
		RETURNING
			cco.auto_kood
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
			.assertQuery(
				"select count(1) from auto_kategooria where auto_kategooria_kood = 1",
				List.of(List.of((long) 1)),
				"Category 1 exists"
			)
			.assertQuery(
				"select auto_kood, auto_kategooria_kood from auto_kategooria_omamine",
				List.of(),
				"No categories assigned"
			)
			.exec("select add_car_to_category (?, ?)", 1, (short) 1)
			.assertQuery(
				"select auto_kood, auto_kategooria_kood from auto_kategooria_omamine",
				List.of(),
				"No categories assigned, since car is in waiting"
			)
		;
		this.sdb
			.setCarStatusActive(1)
		;

		this
			.assertQuery(
				"select auto_kood, auto_seisundi_liik_kood from auto",
				List.of(List.of(1, 2)),
				"Car 1 was moved to active"
			)
			.exec("select add_car_to_category (?, ?)", 1, (short) 1)
			.assertQuery(
				"select auto_kood, auto_kategooria_kood from auto_kategooria_omamine",
				List.of(List.of(1, 1)),
				"Category was assigned"
			)
		;
	}
}
