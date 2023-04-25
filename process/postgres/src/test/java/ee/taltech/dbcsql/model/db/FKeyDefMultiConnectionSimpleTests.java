package ee.taltech.dbcsql.model.db;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.model.db.FKeyBuilder;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.db.TableDefBuilder;

public class FKeyDefMultiConnectionSimpleTests
{
	private DatabaseDef db;
	private TableDef test;
	private TableDef test_multi;
	private FKey id1;
	private FKey id2;

	@BeforeMethod
	public void init()
	{
		this.test = new TableDefBuilder()
			.withName("test", "public.test")
			.withColumn("id", "INTEGER")
		.build()
		;
		this.test_multi = new TableDefBuilder()
			.withName("test_multi", "public.test_multi")
			.withColumn("id", "INTEGER")
			.withColumn("test_id1", "INTEGER")
			.withColumn("test_id2", "INTEGER")
		.build()
		;

		this.id1 = new FKeyBuilder()
			.withName("id1")
			.betweenTables(this.test, this.test_multi)
			.connectColumns("id", "test_id1")
		.build()
		;

		this.id2 = new FKeyBuilder()
			.withName("id2")
			.betweenTables(this.test, this.test_multi)
			.connectColumns("id", "test_id2")
		.build()
		;

		this.db = new DatabaseDefBuilder()
			.withTable(this.test)
			.withTable(this.test_multi)
			.withConnection(this.id1)
			.withConnection(this.id2)
		.build()
		;
	}

	@Test
	public void testMultiConnection()
	{
		Assert.assertEquals(
			db.getConnection("public.test", "public.test_multi", "id1"),
			List.of(this.id1)
		);
		Assert.assertEquals(
			db.getConnection("public.test_multi", "public.test", "id1"),
			List.of(this.id1)
		);

		Assert.assertEquals(
			db.getConnection("public.test", "public.test_multi", "id2"),
			List.of(this.id2)
		);
		Assert.assertEquals(
			db.getConnection("public.test_multi", "public.test", "id2"),
			List.of(this.id2)
		);
	}
}
