package ee.taltech.dbcsql.model.db;

import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.db.DBCommands;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.model.db.FKeyBuilder;
import ee.taltech.dbcsql.test.db.DBTestCommands;
import ee.taltech.dbcsql.test.db.PostgresTestDriver;

public class FKeyDefTests
{
	@Test
	public void testNoConnection()
	{
		DBCommands cmd = new DBCommands(new PostgresTestDriver());
		cmd
			.execute("""
			create table parent
			(
				id int
			)
			""")
			.execute("""
			create table child
			(
				id int,
				parent_id int
			)
			""")
		;
		DatabaseDef db = cmd.readDb();

		Assert.assertEquals(db.getConnection("child", "parent"), Set.of());
		Assert.assertEquals(db.getConnection("test", "not a table"), Set.of());
	}

	@Test
	public void testForeignKeyConnection()
	{
		DBCommands cmd = new DBCommands(new PostgresTestDriver());
		cmd
			.execute("""
			create table parent
			(
				id int,
				primary key(id)
			)
			""")
			.execute("""
			create table child
			(
				id int,
				parent_id int,
				constraint fkey foreign key(parent_id) references parent(id)
			)
			""")
		;
		DatabaseDef actual = cmd.readDb();

		DatabaseDef expected =
			new DatabaseDefBuilder()
				.makeTable()
					.withName("parent", "public.parent")
					.withColumn("id", "INTEGER")
				.build()
				.makeTable()
					.withName("child", "public.child")
					.withColumn("id", "INTEGER")
					.withColumn("parent_id", "INTEGER")
				.build()
				.makeConnection()
					.withName("fkey")
					.betweenTables("public.parent", "public.child")
					.connectColumns("id", "parent_id")
				.build()
			.build()
		;

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testChainConnection()
	{
		DBTestCommands cmd = new DBTestCommands(new PostgresTestDriver());
		cmd.loadTestDatabase();
		DatabaseDef db = cmd.readDb();

		List<FKey> expected = List.of(
			new FKeyBuilder()
				.withName("fk_chain_b")
				.betweenTables(db.getTableDB("public.chain_a"), db.getTableDB("public.chain_b"))
				.connectColumns("chain_a_id", "chain_a_id")
			.build(),
			new FKeyBuilder()
				.withName("fk_chain_c")
				.betweenTables(db.getTableDB("public.chain_b"), db.getTableDB("public.chain_c"))
				.connectColumns("chain_b_id", "chain_b_id")
			.build(),
			new FKeyBuilder()
				.withName("fk_chain_d")
				.betweenTables(db.getTableDB("public.chain_c"), db.getTableDB("public.chain_d"))
				.connectColumns("chain_c_id", "chain_c_id")
			.build()
		);

		Assert.assertEquals(
			db.getConnection("public.chain_a", "public.chain_d"),
			expected,
			"There must be a connection between chain_a and chain_d"
		);
	}
}
