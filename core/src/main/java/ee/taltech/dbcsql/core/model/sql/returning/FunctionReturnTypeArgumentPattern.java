package ee.taltech.dbcsql.core.model.sql.returning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ee.taltech.dbcsql.core.model.dsl.argument.ArgumentDef;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public class FunctionReturnTypeArgumentPattern implements FunctionReturnTypePattern
{
	private Map<String, String> typage = new HashMap<>();

	public FunctionReturnTypeArgumentPattern(Collection<ArgumentDef> args)
	{
		args
			.forEach(x -> this.typage.put(x.getAlias(), x.getType()))
		;
	}

	@Override
	public boolean matches(ReturnValue returnValue)
	{
		return this
			.typage
			.containsKey(returnValue.getSymbolValue())
		;
	}

	@Override
	public String getReturnType(ReturnValue returnValue)
	{
		return this
			.typage
			.get(returnValue.getSymbolValue())
		;
	}
}
