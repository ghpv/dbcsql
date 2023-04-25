package ee.taltech.dbcsql.core.phase.output;

import java.util.HashSet;
import java.util.Set;

import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class RestrictionRegistry
{
	private RestrictionDef data = new RestrictionDef();
	private Set<String> ignored = new HashSet<>();

	public void resetRestrictions()
	{
		this.data = new RestrictionDef();
		this.ignored = new HashSet<>();
	}

	public RestrictionRegistry extendRestriction(RestrictionDef res)
	{
		res
			.clone()
			.getRestrictions()
			.stream()
			.forEach(x -> this.extendRestriction(x))
		;
		return this;
	}

	public RestrictionRegistry extendRestriction(Restriction res)
	{
		this
			.data
			.extendWith(res.clone())
		;
		return this;
	}

	public void addIgnoredVariable(VariableDef var)
	{
		this.ignored.add(this.getKey(var));
	}
	public boolean isIgnored(VariableDef var)
	{
		return this.ignored.contains(this.getKey(var));
	}

	private String getKey(VariableDef var)
	{
		return var.getAlias();
	}

	public RestrictionDef getRestrictions()
	{
		return this.data;
	}
}
