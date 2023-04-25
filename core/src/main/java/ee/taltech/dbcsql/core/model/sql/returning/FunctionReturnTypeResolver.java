package ee.taltech.dbcsql.core.model.sql.returning;

import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public abstract class FunctionReturnTypeResolver
{
	private List<FunctionReturnTypePattern> patterns = new LinkedList<>();

	public String resolveReturnType(ReturnValue returnValue)
	{
		for (FunctionReturnTypePattern pat: patterns)
		{
			if (pat.matches(returnValue))
			{
				return pat.getReturnType(returnValue);
			}
		}
		throw new RuntimeException("Could not figure out the return type for: " + returnValue);
	}

	protected FunctionReturnTypeResolver addPattern(FunctionReturnTypePattern pattern)
	{
		this.patterns.add(pattern);
		return this;
	}
}
