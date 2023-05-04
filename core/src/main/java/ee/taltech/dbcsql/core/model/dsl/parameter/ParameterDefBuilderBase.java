package ee.taltech.dbcsql.core.model.dsl.parameter;

public class ParameterDefBuilderBase<BuilderT extends ParameterDefBuilderBase<BuilderT>>
{
	protected ParameterDef data = new ParameterDef();

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
