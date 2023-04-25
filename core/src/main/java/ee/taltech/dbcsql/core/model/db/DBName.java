package ee.taltech.dbcsql.core.model.db;

import java.util.Objects;
import java.util.Optional;

public class DBName extends StringName
{
	private Optional<String> schema = Optional.empty();

	public DBName(String potentiallyDottedName)
	{
		super(potentiallyDottedName);
		int idx = potentiallyDottedName.indexOf(".");

		if (idx != -1)
		{
			String schema = potentiallyDottedName.substring(0, idx);
			String name = potentiallyDottedName.substring(idx+1);
			this.setSchema(schema);
			this.setName(name);
		}
	}

	public DBName(String schema, String name)
	{
		super(name);
		this.setSchema(schema);
	}

	public Optional<String> getSchema()
	{
		return schema;
	}

	public void setSchema(String schema)
	{
		this.schema = Optional.ofNullable(schema);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getSchema().isPresent())
		{
			sb
				.append(this.getSchema().get())
				.append(".")
			;
		}
		return sb
			.append(this.getName())
			.toString()
		;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DBName))
		{
			return super.equals(obj);
		}
		DBName other = (DBName) obj;
		return true
			&& this.getSchema().equals(other.getSchema())
			&& this.getName().equals(other.getName())
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.getSchema(),
			this.getName()
		);
	}
}
