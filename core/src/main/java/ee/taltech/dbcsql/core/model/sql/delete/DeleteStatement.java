package ee.taltech.dbcsql.core.model.sql.delete;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.model.sql.StatementVisitor;
import ee.taltech.dbcsql.core.model.sql.uvalues.UniqueValues;
import ee.taltech.dbcsql.core.model.sql.where.WhereClause;

public class DeleteStatement implements Statement
{
	private String table;
	private UniqueValues using = null;
	private WhereClause where = null;
	private ReturnValue returnValue = null;

	public DeleteStatement()
	{
	}

	public DeleteStatement(String table)
	{
		this.setTable(table);
	}

	public void setTable(String table)
	{
		this.table = table;
	}

	public void setWhere(WhereClause where)
	{
		this.where = where;
	}

	public void setUsing(UniqueValues using)
	{
		this.using = using;
	}

	public String getTable()
	{
		return this.table;
	}

	public WhereClause getWhere()
	{
		return this.where;
	}

	public UniqueValues getUsing()
	{
		return this.using;
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
			.append("delete from ")
			.append(this.getTable())
			.append(" using = ")
			.append(this.using)
			.append(" where = ")
			.append(this.where)
			.toString()
		;
	}
}
