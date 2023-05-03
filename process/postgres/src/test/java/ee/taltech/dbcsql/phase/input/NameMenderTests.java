package ee.taltech.dbcsql.phase.input;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.input.NameMender;
import ee.taltech.dbcsql.core.phase.input.NameMenderException;

public class NameMenderTests
{
	private NameMender mender;
	private static final String POSITIVE_PROVIDER_NAME = "positive_mend";
	private static final String EXCEPTION_PROVIDER_NAME = "exceptional_mend";

	@BeforeMethod
	public void setup()
	{
		this.mender = new NameMender();
	}

	@DataProvider(name = POSITIVE_PROVIDER_NAME)
	public Object[][] createPositive()
	{
		return new Object[][]
		{
			{"", "wow", "wow", "normal translation"},
			{"", "wowõäöüÕÄÖÜ", "wowoaoy_oaoy", "estonian must be changed"},
			{"", "no spaces", "no_spaces", "spaces must be changed to underscores"},
			{"", "no spaces nameõäöü", "no_spaces_nameoaoy", "estonian must be changed, spaces changed to underscore"},
			{"", "noCamelCase", "no_camel_case", "camelCase must be changed to snake_case"},
			{"", "愛と罰jp", "jp", "non-latin must be removed"},
			{"p", "noCamelCase", "p_no_camel_case", "append prefix"},
			{"p", "愛と罰0", "p_0", "can start with digit if we have a prefix"},
			{"p", "_identifier", "_identifier", "_identifier must stay the same"},
			{"", "null", "null", "null must stay the same because we don't verify SQL keywords"},
			{"p", "null", "null", "null must stay the same because we don't verify SQL keywords"},
			{"p", "p_code", "p_p_code", "do not check for prefixes"},
			{"p", "pin", "p_pin", "do not check for prefixes"},
			{"f", "Operation name", "f_operation_name", "lowercase operation name"},
			{"", "0", "_0", "no prefix just gets underscore"},
			{"", "愛と罰0", "_0", "no prefix gets underscore"},
		};
	}

	@Test(dataProvider = POSITIVE_PROVIDER_NAME)
	public void positive(String prefix, String raw, String out, String msg)
	{
		this.mender.setPrefix(prefix);
		this.mender.setVerifySQLKeywords(false);
		Assert.assertEquals(mender.mendName(raw), out, msg);
	}

	@DataProvider(name = EXCEPTION_PROVIDER_NAME)
	public Object[][] createExceptional()
	{
		return new Object[][]
		{
			{"", "", "empty is not permitted"},
			{"p", "", "empty is not permitted"},
			{"p", "愛と罰", "empty is not permitted"},
			{"AZ", "wow", "prefix may contain only [a-z]"},
			{"0", "wow", "prefix may not start with digit"},
		};
	}

	@Test(dataProvider = EXCEPTION_PROVIDER_NAME, expectedExceptions = NameMenderException.class)
	public void exception(String prefix, String raw, String msg)
	{
		this.mender.setPrefix(prefix);
		mender.mendName(raw);
	}

	@Test
	public void noKeywords()
	{
		for (String kw: NameMender.SQL_KEYWORDS)
		{
			try
			{
				this.mender.setPrefix("p");
				this.mender.setVerifySQLKeywords(true);
				this.mender.mendName(kw);
				Assert.fail("Must throw exception");
			}
			catch (NameMenderException e)
			{
				// Expected
			}
		}
	}
}
