package ee.taltech.dbcsql.core.model.dsl.restriction.connection;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ConnectionRestrictionBuilderBase<BuilderT extends ConnectionRestrictionBuilderBase<BuilderT>>
{
	protected ConnectionRestriction data = new ConnectionRestriction();

	@SuppressWarnings("unchecked")
	public BuilderT betweenVariables(VariableDef a, VariableDef b)
	{
		this.data.setConnection(a, b);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNames(List<String> names)
	{
		this.data.setNames(names);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNewValue(boolean value)
	{
		this.data.setNewLink(value);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withUnlink(boolean value)
	{
		this.data.setUnlink(value);
		return (BuilderT) this;
	}
}
