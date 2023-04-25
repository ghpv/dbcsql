package ee.taltech.dbcsql.core.model;

import java.util.Objects;

public class Pair<KeyType, ValueType>
{
	private KeyType key;
	private ValueType value;

	public Pair()
	{
	}

	public Pair(KeyType key, ValueType value)
	{
		this.setKey(key);
		this.setValue(value);
	}
	public KeyType getKey()
	{
		return key;
	}
	public void setKey(KeyType column)
	{
		this.key = column;
	}
	public ValueType getValue()
	{
		return value;
	}
	public void setValue(ValueType value)
	{
		this.value = value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Pair))
		{
			return super.equals(obj);
		}
		Pair<?, ?> other = (Pair<?, ?>) obj;
		return true
			&& this.key.equals(other.key)
			&& this.value.equals(other.value)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.key,
			this.value
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.key)
			.append("=")
			.append(this.value)
			.toString()
		;
	}
}
