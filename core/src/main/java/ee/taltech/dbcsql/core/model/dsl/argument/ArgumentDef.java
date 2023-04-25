package ee.taltech.dbcsql.core.model.dsl.argument;

public class ArgumentDef
{
	private String alias;
	private String type;

	public ArgumentDef()
	{
	}

	public ArgumentDef(String alias, String type)
	{
		this.setAlias(alias);
		this.setType(type);
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		return new StringBuilder(alias)
			.append(" ")
			.append(type)
			.toString()
		;
	}
}
