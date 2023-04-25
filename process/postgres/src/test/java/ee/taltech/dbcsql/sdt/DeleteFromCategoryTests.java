package ee.taltech.dbcsql.sdt;

import java.util.List;

import org.testng.annotations.Test;

public class DeleteFromCategoryTests extends SampleDataTest
{
	private static final String CONTRACT = "08_delete_car_from_category.dsl";
	private static final String EXPECTED_OUTPUT = """
	CREATE OR REPLACE FUNCTION delete_car_from_category
	(
		p_car_id INTEGER,
		p_car_category_id SMALLINT
	)
	RETURNS INTEGER
	LANGUAGE SQL SECURITY DEFINER
	SET SEARCH_PATH TO 'public', 'pg_temp'
	BEGIN ATOMIC
		DELETE FROM
			auto_kategooria_omamine as cco
		USING
			auto_seisundi_liik as status,
			auto as c,
			auto_kategooria as category
		WHERE
			(
				status.nimetus in ('Aktiivne', 'Mitteaktiivne')
				AND c.auto_kood = p_car_id
				AND c.auto_seisundi_liik_kood = status.auto_seisundi_liik_kood
				AND c.auto_kood = cco.auto_kood
				AND category.auto_kategooria_kood = p_car_category_id
				AND category.auto_kategooria_kood = cco.auto_kategooria_kood
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
			.addCar(1)
			.addCar(2)
			.addCarToCategory(1, (short) 2)
			.addCarToCategory(2, (short) 2)
		;
		this
			.exec("select delete_car_from_category(2, 2::smallint)")
			.assertQuery(
				"select auto_kood from auto_kategooria_omamine",
				List.of(List.of(1), List.of(2)),
				"Category must not be deleted as it's in wrong status"
			)
		;

		this.sdb.setCarStatusActive(2);
		this
			.exec("select delete_car_from_category(2, 2::smallint)")
			.assertQuery(
				"select auto_kood from auto_kategooria_omamine",
				List.of(List.of(1)),
				"Car category must be deleted"
			)
		;
	}
}
