package ee.taltech.dbcsql.core.model.dsl.variable;

import java.util.HashMap;
import java.util.Map;

public class VariableRegistry
{
	Map<String, VariableDef> data = new HashMap<>();

	public VariableDef find(String name)
	{
		VariableDef ans = this.data.get(name);
		return ans;
	}

	public VariableRegistry registerVariable(VariableDef var)
	{
		assert this.data.get(var.getAlias()) == null: "Variable already exists: " + var.getAlias();
		this.data.put(var.getAlias(), var);
		return this;
	}
}
