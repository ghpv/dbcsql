package ee.taltech.dbcsql.core.model.sql.insert;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.model.sql.StatementVisitor;
import ee.taltech.dbcsql.core.model.sql.StringColumnEquality;
import ee.taltech.dbcsql.core.model.sql.uvalues.UniqueValues;
import ee.taltech.dbcsql.core.model.sql.where.WhereClause;

public class InsertStatement implements Statement
{
	private String table;
	private List<StringColumnEquality> values = new LinkedList<>();
	private UniqueValues relatedTables = null;
	private WhereClause where = null;
	private ReturnValue returnValue = null;

	public InsertStatement()
	{
	}

	public InsertStatement(String table)
	{
		this.setTable(table);
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public String getTable()
	{
		return this.table;
	}

	public InsertStatement addValue(ColumnDef col, String value)
	{
		return this.addValue(new StringColumnEquality(col, value));
	}

	public InsertStatement addValue(String col, String value)
	{
		return this.addValue(new StringColumnEquality(col, value));
	}

	public InsertStatement addValue(StringColumnEquality val)
	{
		this.values.add(val);
		return this;
	}

	public List<String> getColumns()
	{
		return this
			.values
			.stream()
			.map(x -> x.getKey())
			.collect(Collectors.toList())
		;
	}

	public List<String> getValues()
	{
		return this
			.values
			.stream()
			.map(x -> x.getValue())
			.collect(Collectors.toList())
		;
	}

	public UniqueValues getRelatedTables()
	{
		return this.relatedTables;
	}

	public InsertStatement setRelatedTables(UniqueValues relatedTables)
	{
		this.relatedTables = relatedTables;
		return this;
	}

	public WhereClause getWhere()
	{
		return this.where;
	}

	public InsertStatement setWhere(WhereClause clause)
	{
		this.where = clause;
		return this;
	}

	public ReturnValue getReturnValue()
	{
		return returnValue;
	}

	public void setReturnValue(ReturnValue returnValue)
	{
		this.returnValue = returnValue;
	}

	@Override
	public <T> T accept(StatementVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("insert into ")
			.append(this.getTable())
			.append("(")
			.append(this.getColumns())
			.append(") values (")
			.append(this.getValues())
			.append(")")
			.toString()
		;
	}
}
