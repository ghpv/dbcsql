package ee.taltech.dbcsql.core.model.dsl.post;

import java.util.List;

import ee.taltech.dbcsql.core.model.Pair;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class VariableLink extends Pair<VariableDef, List<String>>
{
	private boolean unlink = false;
	public VariableLink(VariableDef var, List<String> names)
	{
		super(var, names);
	}

	public boolean isUnlink()
	{
		return unlink;
	}
	public void setUnlink(boolean unlink)
	{
		this.unlink = unlink;
	}
}
