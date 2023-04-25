package ee.taltech.dbcsql.uc;

import java.io.ByteArrayInputStream;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;
import ee.taltech.dbcsql.core.phase.input.InputPhase;

public class ContextErrorHandlingTests extends UseCaseTest
{
	private static final String TEST_PROVIDER_NAME = "PROVIDER";

	@DataProvider(name = TEST_PROVIDER_NAME)
	public Object[][] create()
	{
		return new Object[][]
		{
			{
				"""
				"""
				,
				"line 1:0 mismatched input '<EOF>' expecting {'table', 'connection', 'identifier'}"
			},
			{
				"""
				table
				{
				}
				"""
				,
				"line 2:0 missing ID at '{'"
			},
			{
				"""
				table a
				{
					id INTEGER;
				};
				table b
				{
					id INTEGER;
				};
				connection between c and d
				{
					id = id;
				};
				"""
				,
				"Table not found for DB name 'c'"
			},
			{
				"""
				table a
				{
					id INTEGER;
				};
				table b
				{
					id INTEGER;
				};
				connection between a and b
				{
					id = sub_id;
				};
				"""
				,
				"Column 'sub_id' in table b(b) is not found"
			},
			{
				"""
				table a extends wow
				{
				};
				"""
				,
				"Did not find table to extend: 'wow'"
			},
			{
				"""
				table a
				{
					id INTEGER;
				};
				identifier for test is id;
				"""
				,
				"No such table 'test'"
			},
			{
				"""
				table a
				{
					id INTEGER;
				};
				identifier for a is not_id;
				"""
				,
				"Table 'a' has no column 'not_id' to use as identifier"
			},
		};
	}

	@Test(dataProvider = TEST_PROVIDER_NAME)
	public void test(String operation, String msg)
	{
		try
		{
			InputPhase input = new InputPhase();
			input
				.makeGenerationContext()
					.withContextFromStream(new ByteArrayInputStream(operation.getBytes()));
			;
			Assert.fail("Input is incorrect, this should have thrown exception");
		}
		catch (TranslatorInputException e)
		{
			Assert.assertEquals(e.getMessage(), msg);
		}
	}
}
