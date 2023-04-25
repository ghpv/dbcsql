package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ComparisonType
{
	EQUALS("=", "=="),
	NOT_EQUALS("<>", "!="),
	LESS("<"),
	GREATER(">"),
	LESS_EQUALS("<="),
	GREATER_EQUALS(">="),
	IS("is"),
	IS_NOT("is not", "isnot"),
	NOT_IN("not in", "notin"),
	IN("in"),
	;

	private List<String> symbols;
	private static Map<String, ComparisonType> MAPPING = new HashMap<>();
	static
	{
		for (ComparisonType t: values())
		{
			for (String s: t.symbols)
			{
				MAPPING.put(s, t);
			}
		}
	}
	private ComparisonType(List<String> sym)
	{
		this.symbols = sym;
	}
	private ComparisonType(String... sym)
	{
		this(List.of(sym));
	}
	private ComparisonType(String sym)
	{
		this(List.of(sym));
	}

	@Override
	public String toString()
	{
		return this.symbols.get(0);
	}

	public static ComparisonType fromSymbol(String sym)
	{
		return MAPPING.get(sym);
	}
}
