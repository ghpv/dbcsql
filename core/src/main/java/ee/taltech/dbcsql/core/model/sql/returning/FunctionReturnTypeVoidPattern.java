package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public class FunctionReturnTypeVoidPattern implements FunctionReturnTypePattern
{
	@Override
	public boolean matches(ReturnValue returnValue)
	{
		return returnValue == null;
	}

	@Override
	public String getReturnType(ReturnValue returnValue)
	{
		return null;
	}
}
