package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public class FunctionReturnTypeVarcharPattern implements FunctionReturnTypePattern
{
	@Override
	public boolean matches(ReturnValue returnValue)
	{
		String sym = returnValue.getSymbolValue();
		return
			sym.startsWith("'")
			&& sym.endsWith("'")
		;
	}

	@Override
	public String getReturnType(ReturnValue returnValue)
	{
		return "VARCHAR";
	}
}
