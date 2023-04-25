package ee.taltech.dbcsql.core.model.sql.uvalues;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UniqueValues
{
	private List<String> values = new LinkedList<>();
	private Set<String> seenValues = new HashSet<>();

	public UniqueValues()
	{
	}


	public void setValues(Collection<String> clauses)
	{
		clauses
			.stream()
			.forEach(x -> this.addValue(x))
		;
	}

	public void addValue(String clause)
	{
		if (this.seenValues.add(clause))
		{
			this.values.add(clause);
		}
	}

	public void removeValue(String clause)
	{
		if (this.seenValues.remove(clause))
		{
			this.values.remove(clause);
		}
	}

	public List<String> getValues()
	{
		return values;
	}

	@Override
	public String toString()
	{
		return this.values.toString();
	}
}
