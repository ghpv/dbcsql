package ee.taltech.dbcsql.core.model.dsl.variable;

import ee.taltech.dbcsql.core.model.db.TableDef;

public class VariableDefBuilderBase<BuilderT extends VariableDefBuilderBase<BuilderT>>
{
	VariableDef data = new VariableDef();

	@SuppressWarnings("unchecked")
	public BuilderT withAlias(String alias)
	{
		this.data.setAlias(alias);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withTable(TableDef table)
	{
		this.data.setTable(table);
		return (BuilderT) this;
	}
}
