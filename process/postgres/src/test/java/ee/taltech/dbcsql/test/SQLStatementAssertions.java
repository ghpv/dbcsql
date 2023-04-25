package ee.taltech.dbcsql.test;

import org.testng.Assert;

public class SQLStatementAssertions
{
	private SQLStatementAssertions()
	{
	}

	public static void assertStatementsEqual(String actual, String expected)
	{
		assertStatementsEqual(actual, expected, null);
	}

	public static void assertStatementsEqual(String actual, String expected, String msg)
	{
		Assert.assertEquals(
			formatString(actual),
			formatString(expected),
			msg
		);
	}

	private static String formatString(String s)
	{
		return s
			.replaceAll("\\s+", " ")
			.strip()
		;
	}
}
