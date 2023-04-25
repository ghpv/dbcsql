package ee.taltech.dbcsql.model.db;

import java.io.ByteArrayInputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.phase.input.context.ContextInputParser;

public class DatabaseDefFromFileTests
{
	@Test
	public void testReadingDbForTable()
	{
		ByteArrayInputStream stream = new ByteArrayInputStream("""
		table t1
		{
			t1_id INTEGER;
			t1_data VARCHAR;
		};
		table t2
		{
			t2_id INTEGER;
			t2_data VARCHAR;
		};

		connection between t1 and t2
		{
			t1_id = t2_id;
		};
		""".getBytes());
		DatabaseDef actual = new ContextInputParser().parse(stream);

		DatabaseDef expected =
			new DatabaseDefBuilder()
				.makeTable()
					.withName("t1")
					.withColumn("t1_id", "INTEGER")
					.withColumn("t1_data", "VARCHAR")
				.build()
				.makeTable()
					.withName("t2")
					.withColumn("t2_id", "INTEGER")
					.withColumn("t2_data", "VARCHAR")
				.build()
				.makeConnection()
					.betweenTables("t1", "t2")
					.connectColumns("t1_id", "t2_id")
				.build()
			.build()
		;
		Assert.assertEquals(actual, expected);
	}
}
