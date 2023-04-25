package ee.taltech.dbcsql.model.db;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.DBName;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.db.TableDefBuilder;
import ee.taltech.dbcsql.test.DBTest;

public class TableDefTests extends DBTest
{
	@Test
	public void testReadingDbForTable()
	{
		db.execute("""
		CREATE TABLE TEST
		(
			CODE CHAR(5),
			TITLE VARCHAR(40),
			DID INTEGER,
			DATEPROD DATE,
			INSERTTS TIMESTAMP,
			LEN INTERVAL HOUR TO MINUTE,
			CONSTRAINT CODETITLE PRIMARY KEY(CODE,TITLE)
		);
		""");

		TableDef actual = this.db.readDb().getTable(new DBName("public", "test"));
		TableDef expected = new TableDefBuilder()
			.withName("test", "public.test")
			.withColumn("code", "CHAR")
			.withColumn("title", "VARCHAR")
			.withColumn("did", "INTEGER")
			.withColumn("dateProd", "DATE")
			.withColumn("len", "INTERVAL")
			.withColumn("insertts", "TIMESTAMP")
			.build()
		;

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testReadingDbForMultipleTables()
	{
		db.execute("""
		CREATE TABLE TEST
		(
			CODE CHAR(5),
			TITLE VARCHAR(40),
			DID INTEGER,
			DATEPROD DATE,
			INSERTTS TIMESTAMP,
			LEN INTERVAL HOUR TO MINUTE,
			CONSTRAINT CODETITLE PRIMARY KEY(CODE,TITLE)
		);
		""");

		db.execute("""
		CREATE TABLE TEST2
		(
			AMAZING BIGINT,
			WOW INT8,
			TEST TEXT
		);
		""");

		DatabaseDef actual = this.db.readDb();

		DatabaseDef expected = new DatabaseDefBuilder()
			.makeTable()
				.withName("TEST", "public.TEST")
				.withColumn("CODE", "CHAR")
				.withColumn("TITLE", "VARCHAR")
				.withColumn("DID", "INTEGER")
				.withColumn("DATEPROD", "DATE")
				.withColumn("LEN", "INTERVAL")
				.withColumn("INSERTTS", "TIMESTAMP")
			.build()
			.makeTable()
				.withName("TEST2", "public.TEST2")
				.withColumn("AMAZING", "BIGINT")
				.withColumn("WOW", "BIGINT")
				.withColumn("TEST", "TEXT")
			.build()
			.build()
		;

		Assert.assertEquals(actual, expected);
	}
}
