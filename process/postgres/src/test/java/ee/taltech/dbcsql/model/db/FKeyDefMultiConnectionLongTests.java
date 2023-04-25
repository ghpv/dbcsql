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

public class FKeyDefMultiConnectionLongTests
{
	private DatabaseDef db;
	private TableDef test;
	private TableDef test_multi1;
	private TableDef test_multi2;
	private TableDef test_multi3;
	private FKey test_multi1_id1;
	private FKey test_multi1_id2;
	private FKey test_multi1_id3;
	private FKey test_multi2_id1;
	private FKey test_multi2_id2;
	private FKey test_multi2_id3;
	private FKey test_multi3_id1;
	private FKey test_multi3_id2;
	private FKey test_multi3_id3;

	@BeforeMethod
	public void init()
	{
		this.test = new TableDefBuilder()
			.withName("test", "public.test")
			.withColumn("id", "INTEGER")
		.build()
		;
		this.test_multi1 = makeTestMulti(1);
		this.test_multi2 = makeTestMulti(2);
		this.test_multi3 = makeTestMulti(3);

		this.test_multi1_id1 = makeTestMultiFKey("id1", this.test, "id", this.test_multi1, "ref_id1");
		this.test_multi1_id2 = makeTestMultiFKey("id2", this.test, "id", this.test_multi1, "ref_id2");
		this.test_multi1_id3 = makeTestMultiFKey("id3", this.test, "id", this.test_multi1, "ref_id3");

		this.test_multi2_id1 = makeTestMultiFKey("id1", this.test_multi1, "id", this.test_multi2, "ref_id1");
		this.test_multi2_id2 = makeTestMultiFKey("id2", this.test_multi1, "id", this.test_multi2, "ref_id2");
		this.test_multi2_id3 = makeTestMultiFKey("id3", this.test_multi1, "id", this.test_multi2, "ref_id3");

		this.test_multi3_id1 = makeTestMultiFKey("id1", this.test_multi2, "id", this.test_multi3, "ref_id1");
		this.test_multi3_id2 = makeTestMultiFKey("id2", this.test_multi2, "id", this.test_multi3, "ref_id2");
		this.test_multi3_id3 = makeTestMultiFKey("id3", this.test_multi2, "id", this.test_multi3, "ref_id3");

		this.db = new DatabaseDefBuilder()
			.withTable(this.test)
			.withTable(this.test_multi1)
			.withTable(this.test_multi2)
			.withTable(this.test_multi3)
			.withConnection(this.test_multi1_id1)
			.withConnection(this.test_multi1_id2)
			.withConnection(this.test_multi1_id3)
			.withConnection(this.test_multi2_id1)
			.withConnection(this.test_multi2_id2)
			.withConnection(this.test_multi2_id3)
			.withConnection(this.test_multi3_id1)
			.withConnection(this.test_multi3_id2)
			.withConnection(this.test_multi3_id3)
		.build()
		;
	}

	private TableDef makeTestMulti(int multiID)
	{
		return new TableDefBuilder()
			.withName("test_multi" + multiID, "public.test_multi" + multiID)
			.withColumn("id", "INTEGER")
			.withColumn("ref_id1", "INTEGER")
			.withColumn("ref_id2", "INTEGER")
			.withColumn("ref_id3", "INTEGER")
		.build();
	}

	private FKey makeTestMultiFKey(String name, TableDef a, String aCol, TableDef b, String bCol)
	{
		return new FKeyBuilder()
			.withName(name)
			.betweenTables(a, b)
			.connectColumns(aCol, bCol)
		.build();
	}

	@Test
	public void testMultiConnection()
	{
		Assert.assertEquals(
			db.getConnection("public.test", "public.test_multi3", "id1", "id2", "id3"),
			List.of(this.test_multi1_id1, this.test_multi2_id2, this.test_multi3_id3)
		);
	}
}
