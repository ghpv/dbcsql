package ee.taltech.dbcsql.core.db;

import java.util.Map;

public abstract class TypeInterpreter
{
	private Map<String, String> mapping;

	public TypeInterpreter(Map<String, String> mapping)
	{
		this.mapping = mapping;
	}

	public String interprete(String s)
	{
		String t = this.mapping.get(s.toLowerCase());
		assert t != null: "Could not interprete " + s;
		return t;
	}
}
