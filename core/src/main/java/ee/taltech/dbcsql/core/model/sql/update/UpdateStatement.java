package ee.taltech.dbcsql.core.model.sql.update;

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

public class UpdateStatement implements Statement
{
	private String table;
	private List<StringColumnEquality> values = new LinkedList<>();
	private UniqueValues relatedTables = null;
	private WhereClause where = null;
	private ReturnValue returnValue = null;

	public UpdateStatement()
	{
	}

	public UpdateStatement(String table)
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

	public UpdateStatement addValue(ColumnDef col, String value)
	{
		return this.addValue(new StringColumnEquality(col, value));
	}

	public UpdateStatement addValue(String col, String value)
	{
		return this.addValue(new StringColumnEquality(col, value));
	}

	public UpdateStatement addValue(StringColumnEquality val)
	{
		this.values.add(val);
		return this;
	}

	public List<String> getLines()
	{
		return this
			.values
			.stream()
			.map(x -> translateColumnEquality(x))
			.collect(Collectors.toList())
		;
	}

	private String translateColumnEquality(StringColumnEquality col)
	{
		return new StringBuilder()
			.append(col.getKey())
			.append(" = ")
			.append(col.getValue())
			.toString()
		;
	}

	public UniqueValues getRelatedTables()
	{
		return this.relatedTables;
	}

	public UpdateStatement setRelatedTables(UniqueValues relatedTables)
	{
		this.relatedTables = relatedTables;
		return this;
	}

	public WhereClause getWhere()
	{
		return this.where;
	}

	public UpdateStatement setWhere(WhereClause clause)
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
			.append("update ")
			.append(this.getTable())
			.append(" set (")
			.append(this.getLines())
			.append(")")
			.append(" relatedTables = ")
			.append(this.relatedTables)
			.append(" where ")
			.append(this.where)
			.toString()
		;
	}
}
