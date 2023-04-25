package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public class FunctionReturnTypeIntegerPattern implements FunctionReturnTypePattern
{
	@Override
	public boolean matches(ReturnValue returnValue)
	{
		try
		{
			Integer.parseInt(returnValue.getSymbolValue());
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Override
	public String getReturnType(ReturnValue _ignored)
	{
		return "INTEGER";
	}
}
