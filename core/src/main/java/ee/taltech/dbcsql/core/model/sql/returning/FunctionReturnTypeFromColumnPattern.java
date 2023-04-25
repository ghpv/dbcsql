package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public class FunctionReturnTypeFromColumnPattern implements FunctionReturnTypePattern
{
	@Override
	public boolean matches(ReturnValue returnValue)
	{
		return returnValue.getColumn().isPresent();
	}

	@Override
	public String getReturnType(ReturnValue returnValue)
	{
		return returnValue
			.getColumn()
			.get()
			.getType()
		;
	}
}
