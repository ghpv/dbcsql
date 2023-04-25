package ee.taltech.dbcsql.model.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.test.DBTest;

public class ColumnDefTests extends DBTest
{
	@Test
	public void testEquality()
	{
		TableDef table = new TableDef();
		ColumnDef cd = new ColumnDef(table, "a", "VARCHAR");
		ColumnDef cd2 = new ColumnDef(table, "a", "VARCHAR");
		Assert.assertEquals(cd, cd2);
	}

	@Test
	public void testSet()
	{
		TableDef table = new TableDef();
		ColumnDef cd = new ColumnDef(table, "a", "VARCHAR");
		ColumnDef cd2 = new ColumnDef(table, "a", "VARCHAR");
		Set<ColumnDef> cols = new HashSet<>(List.of(
			cd,
			cd2,
			new ColumnDef(table, "a", "VARCHAR")
		));
		Assert.assertEquals(cols, Set.of(new ColumnDef(table, "a", "VARCHAR")));
	}
}
