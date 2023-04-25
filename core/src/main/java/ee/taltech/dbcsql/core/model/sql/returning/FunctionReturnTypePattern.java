package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;

public interface FunctionReturnTypePattern
{
	public boolean matches(ReturnValue returnValue);
	public String getReturnType(ReturnValue returnValue);
}
