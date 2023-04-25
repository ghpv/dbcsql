package ee.taltech.dbcsql.uc;

import java.io.ByteArrayInputStream;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class ContractErrorHandlingTests extends UseCaseTest
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
				"line 1:0 mismatched input '<EOF>' expecting 'operation'"
			},
			{
				"""
				operation
				{
				}
				preconditions
				{
				}
				postconditions
				{
				}
				"""
				,
				"line 2:0 missing ID at '{'"
			},
			{
				"""
				operation op
				{
					p_id INTEGER;
				}
				preconditions
				{
				}
				postconditions
				{
				}
				"""
				,
				"line 3:6 extraneous input 'INTEGER' expecting ';'"
			},
			{
				"""
				operation op
				{
					p_id;
					p_id;
				}
				preconditions
				{
				}
				postconditions
				{
				}
				"""
				,
				"Argument 'p_id' was already given!"
			},
			{
				"""
				operation op
				{
					p_id;
				}
				preconditions
				{
					exists test a;
					exists test a;
				}
				postconditions
				{
				}
				"""
				,
				"Variable for alias 'a' exists already!"
			},
			{
				"""
				operation op
				{
					p_id;
				}
				preconditions
				{
					exists non_existent_table a;
				}
				postconditions
				{
				}
				"""
				,
				"Did not find table 'non_existent_table'!"
			},
			{
				"""
				operation op
				{
					p_id;
				}
				preconditions
				{
					exists test a(no_such_column = p_id);
				}
				postconditions
				{
				}
				"""
				,
				"test(public.test) a does not have column 'no_such_column'!"
			},
			{
				"""
				operation op
				{
					p_id;
				}
				preconditions
				{
					exists test a(id = p_id);
				}
				postconditions
				{
					deleted b;
				}
				"""
				,
				"Did not find variable 'b'!"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
					exists vardata a;
				}
				postconditions
				{
					deleted a return _identifier;
				}
				"""
				,
				"Asked to return identifier, but 'vardata' does not have an identifier!"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
					exists vardata a;
				}
				postconditions
				{
					updated a
					{
						link with nothing;
					};
				}
				"""
				,
				"Did not find variable 'nothing'!"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
					exists vardata a;
				}
				postconditions
				{
					updated a
					{
						unlink from nothing;
					};
				}
				"""
				,
				"Did not find variable 'nothing'!"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
				}
				postconditions
				{
					updated nothing
					{
					};
				}
				"""
				,
				"Did not find variable 'nothing'!"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
					exists test a;
				}
				postconditions
				{
					updated a
					{
						no_such_table = 1;
					};
				}
				"""
				,
				"Did not find column 'no_such_table' for variable test(public.test) a"
			},
			{
				"""
				operation op
				{
				}
				preconditions
				{
				}
				postconditions
				{
					inserted test a
					{
						no_such_table = 1;
					};
				}
				"""
				,
				"Did not find column 'no_such_table' for variable test(public.test) a"
			},
		};
	}

	@Test(dataProvider = TEST_PROVIDER_NAME)
	public void test(String operation, String msg)
	{
		try
		{
			this.generate(new ByteArrayInputStream(operation.getBytes()));
			Assert.fail("Input is incorrect, this should have thrown exception");
		}
		catch (TranslatorInputException e)
		{
			Assert.assertEquals(e.getMessage(), msg);
		}
	}
}
