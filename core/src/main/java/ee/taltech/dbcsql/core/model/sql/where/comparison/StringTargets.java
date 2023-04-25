package ee.taltech.dbcsql.core.model.sql.where.comparison;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class StringTargets
{
	private List<String> targets = new LinkedList<>();
	private boolean singularValue = true;

	public StringTargets()
	{
	}

	public StringTargets(Collection<String> targets)
	{
		this.setTargets(targets);
	}

	public void addTarget(String target)
	{
		this.targets.add(target);
	}
	public int size()
	{
		return this.targets.size();
	}
	public boolean add(String target)
	{
		return this.targets.add(target);
	}

	public void setTargets(Collection<String> targets)
	{
		targets
			.stream()
			.forEach(x -> this.addTarget(x))
		;
	}

	public List<String> getTargets()
	{
		return targets;
	}

	public boolean getSingularValue()
	{
		return singularValue;
	}

	public void setSingularValue(boolean setValue)
	{
		this.singularValue = setValue;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof StringTargets))
		{
			return super.equals(obj);
		}
		StringTargets other = (StringTargets) obj;
		return true
			&& this.targets.equals(other.targets)
		;
	}

	@Override
	public int hashCode()
	{
		return this.targets.hashCode();
	}

	@Override
	public String toString()
	{
		if (this.getSingularValue())
		{
			return this.targets.get(0);
		}
		return new StringBuilder()
			.append("(")
			.append(String.join(", ", this.targets))
			.append(")")
			.toString()
		;
	}
}
