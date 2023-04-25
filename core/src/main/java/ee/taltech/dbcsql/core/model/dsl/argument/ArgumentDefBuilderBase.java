package ee.taltech.dbcsql.core.model.dsl.argument;

public class ArgumentDefBuilderBase<BuilderT extends ArgumentDefBuilderBase<BuilderT>>
{
	protected ArgumentDef data = new ArgumentDef();

	@SuppressWarnings("unchecked")
	public BuilderT withAlias(String alias)
	{
		this.data.setAlias(alias);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withType(String type)
	{
		this.data.setType(type);
		return (BuilderT) this;
	}
}
