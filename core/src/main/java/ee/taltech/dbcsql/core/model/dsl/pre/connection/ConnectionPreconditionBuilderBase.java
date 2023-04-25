package ee.taltech.dbcsql.core.model.dsl.pre.connection;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ConnectionPreconditionBuilderBase<BuilderT extends ConnectionPreconditionBuilderBase<BuilderT>>
{
	protected ConnectionPrecondition data = new ConnectionPrecondition();

	@SuppressWarnings("unchecked")
	public BuilderT betweenVariables(VariableDef a, VariableDef b)
	{
		this.data.setConnection(a, b);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withName(String name)
	{
		this.data.addName(name);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withName(List<String> name)
	{
		this.data.setNames(name);
		return (BuilderT) this;
	}
}
