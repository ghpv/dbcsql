package ee.taltech.dbcsql.core.model.dsl.post;

import java.util.Objects;
import java.util.Optional;

import ee.taltech.dbcsql.core.model.db.ColumnDef;

public class ReturnValue
{
	private String symbolValue;
	private Optional<ColumnDef> column = Optional.empty();

	public ReturnValue()
	{
	}

	public ReturnValue(String symbolValue)
	{
		this.setSymbolValue(symbolValue);
	}

	public ReturnValue(String symbolValue, ColumnDef column)
	{
		this(symbolValue);
		this.setColumn(column);
	}

	public String getSymbolValue()
	{
		return symbolValue;
	}
	public void setSymbolValue(String symbolValue)
	{
		this.symbolValue = symbolValue;
	}
	public Optional<ColumnDef> getColumn()
	{
		return column;
	}
	public void setColumn(ColumnDef column)
	{
		this.column = Optional.of(column);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ReturnValue))
		{
			return super.equals(obj);
		}
		ReturnValue other = (ReturnValue) obj;
		return true
			&& this.symbolValue.equals(other.symbolValue)
			&& this.column.equals(other.column)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.symbolValue,
			this.column
		);
	}

	@Override
	public String toString()
	{
		return new StringBuffer()
			.append(this.column)
			.append(" ")
			.append(this.symbolValue)
			.toString()
		;
	}
}
