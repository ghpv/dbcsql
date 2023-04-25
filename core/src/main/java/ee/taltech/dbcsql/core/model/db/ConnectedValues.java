package ee.taltech.dbcsql.core.model.db;

import java.util.Objects;

public abstract class ConnectedValues <T>
{
	private T a;
	private T b;

	public ConnectedValues(T a, T b)
	{
		this.a = a;
		this.b = b;

		if (this.compare(a, b) > 0)
		{
			this.swapNames();
		}
	}

	protected abstract int compare(T a, T b);

	private void swapNames()
	{
		T tmp = this.a;
		this.a = this.b;
		this.b = tmp;
	}

	public T getA()
	{
		return this.a;
	}

	public T getB()
	{
		return this.b;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ConnectedValues))
		{
			return super.equals(obj);
		}
		ConnectedValues<T> other = (ConnectedValues<T>) obj;
		return true
			&& this.a.equals(other.a)
			&& this.b.equals(other.b)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.a,
			this.b
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.a)
			.append("--")
			.append(this.b)
			.toString()
		;
	}
}
