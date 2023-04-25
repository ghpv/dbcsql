package ee.taltech.dbcsql.model.db;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.ConnectedStrings;

public class StringConnectionTests
{
	@Test
	public void testSameConnection()
	{
		String child = "child";
		String parent = "parent";
		ConnectedStrings a = new ConnectedStrings(child, parent);
		ConnectedStrings b = new ConnectedStrings(parent, child);

		Assert.assertEquals(a, b, "Must be equal");
	}
}
