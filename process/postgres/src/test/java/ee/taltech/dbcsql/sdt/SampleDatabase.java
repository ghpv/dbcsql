package ee.taltech.dbcsql.sdt;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Random;

import ee.taltech.dbcsql.test.db.DBTestCommands;

public class SampleDatabase
{
	private DBTestCommands db;
	private Random rnd = new Random();

	public SampleDatabase(DBTestCommands db)
	{
		this.db = db;
		this.setup();
	}

	public void setup()
	{
		this
			.addCountry("EST", "Estonia")
			.addPerson(1)
			.addWorker(1)
		;
	}

	private static final String ADD_COUNTRY_QUERY = "insert into riik values (?, ?)";
	public SampleDatabase addCountry(String code, String name)
	{
		return this.exec(ADD_COUNTRY_QUERY, code, name);
	}

	private static final String ADD_PERSON = "insert into isik values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public SampleDatabase addPerson(
		int id,
		String email,
		String pin,
		String countryCode,
		short statusCode,
		Timestamp regTime,
		Date birthday,
		String name,
		String lastName,
		String residence
	) {
		return this.exec(ADD_PERSON, id, email, pin, countryCode, statusCode, regTime, birthday, name, lastName, residence);
	}
	public SampleDatabase addPerson(int id)
	{
		return this.addPerson(
			id,
			"email@",
			"00000000000",
			"EST",
			(short) 1,
			Timestamp.valueOf("2020-01-01 00:00:00"),
			Date.valueOf("1900-01-01"),
			"a",
			"a",
			"1 wow"
		);
	}

	private static final String ADD_WORKER = "insert into tootaja values (?, ?, ?)";
	public SampleDatabase addWorker(int id, short statusCode, Integer mentor)
	{
		return this.exec(ADD_WORKER, id, statusCode, mentor);
	}
	public SampleDatabase addWorker(int id)
	{
		return this.addWorker(id, (short) 1, null);
	}

	private static final String ADD_CAR = "insert into auto values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public SampleDatabase addCar(
		int id,
		String name,
		String model,
		short releaseYear,
		String regCode,
		int seats,
		double engineVolume,
		String vinCode,
		Timestamp regTime,
		int registrarId,
		short fuelType,
		short carStatus,
		short carBrand
	) {
		return this.exec(ADD_CAR, id, name, model, releaseYear, regCode, seats, engineVolume, vinCode, regTime, registrarId, fuelType, carStatus, carBrand);
	}
	public SampleDatabase addCar(int id)
	{
		String name = this.randomString(10);
		String vin = this.randomString(11);
		return this.addCar(
			id,
			name,
			"model",
			(short) 2021,
			"00000000",
			4,
			4.3,
			vin,
			Timestamp.valueOf("2022-01-01 00:00:00"),
			1,
			(short) 1,
			(short) 1,
			(short) 1
		);
	}

	private static final String MOD_CAR_STATUS_BY_ID = "update auto set auto_seisundi_liik_kood = ? where auto_kood = ?";
	public SampleDatabase modCarStatusByID(int carID, short newStatus)
	{
		return this.exec(MOD_CAR_STATUS_BY_ID, newStatus, carID);
	}
	public SampleDatabase setCarStatusWaiting(int carID)
	{
		return this.modCarStatusByID(carID, (short) 1);
	}
	public SampleDatabase setCarStatusActive(int carID)
	{
		return this.modCarStatusByID(carID, (short) 2);
	}
	public SampleDatabase setCarStatusInactive(int carID)
	{
		return this.modCarStatusByID(carID, (short) 3);
	}
	public SampleDatabase setCarStatusDone(int carID)
	{
		return this.modCarStatusByID(carID, (short) 4);
	}

	private static final String ADD_CAR_TO_CATEGORY = "insert into auto_kategooria_omamine values (?, ?)";
	public SampleDatabase addCarToCategory(int carID, short categoryID)
	{
		return this.exec(ADD_CAR_TO_CATEGORY, carID, categoryID);
	}
	public SampleDatabase addCarToCategoryFamily(int carID)
	{
		return this.addCarToCategory(carID, (short) 1);
	}
	public SampleDatabase addCarToCategorySmall(int carID)
	{
		return this.addCarToCategory(carID, (short) 2);
	}
	public SampleDatabase addCarToCategoryMinibus(int carID)
	{
		return this.addCarToCategory(carID, (short) 3);
	}
	public SampleDatabase addCarToCategoryLuxury(int carID)
	{
		return this.addCarToCategory(carID, (short) 4);
	}
	public SampleDatabase addCarToCategoryTown(int carID)
	{
		return this.addCarToCategory(carID, (short) 5);
	}
	public SampleDatabase addCarToCategoryCheap(int carID)
	{
		return this.addCarToCategory(carID, (short) 6);
	}
	public SampleDatabase addCarToCategoryCommercial(int carID)
	{
		return this.addCarToCategory(carID, (short) 7);
	}

	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private String randomString(int length)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; ++i)
		{
			int idx = this.rnd.nextInt(ALPHABET.length());
			sb.append(ALPHABET.charAt(idx));
		}
		return sb.toString();
	}

	public SampleDatabase exec(String qry, Object... args)
	{
		this.db.execute(qry, args);
		return this;
	}
}
