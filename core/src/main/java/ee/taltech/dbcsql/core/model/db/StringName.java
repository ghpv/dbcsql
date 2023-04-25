package ee.taltech.dbcsql.core.model.db;

import java.util.Objects;

public class StringName implements Comparable<StringName>
{
	private String name;

	public StringName(String name)
	{
		this.setName(name);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		if (!name.contains("\""))
		{
			name = name.toLowerCase();
		}
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof StringName))
		{
			return super.equals(obj);
		}
		StringName other = (StringName) obj;
		return true
			&& this.name.equals(other.name)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.name
		);
	}

	@Override
	public int compareTo(StringName other)
	{
		return this.name.compareTo(other.name);
	}
}
