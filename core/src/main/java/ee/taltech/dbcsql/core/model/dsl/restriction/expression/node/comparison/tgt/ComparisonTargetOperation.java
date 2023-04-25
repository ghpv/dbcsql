package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt;

public enum ComparisonTargetOperation
{
	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVIDE("/"),
	CONCAT("||")
	;

	private String symbol;
	private ComparisonTargetOperation(String sym)
	{
		this.symbol = sym;
	}

	public static ComparisonTargetOperation fromSymbol(String sym)
	{
		for (ComparisonTargetOperation cto: ComparisonTargetOperation.values())
		{
			if (cto.symbol.equals(sym))
			{
				return cto;
			}
		}
		throw new RuntimeException("No enum for " + sym);
	}

	public String getSymbol()
	{
		return this.symbol;
	}
}
