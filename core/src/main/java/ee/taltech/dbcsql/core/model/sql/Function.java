package ee.taltech.dbcsql.core.model.sql;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.sql.uvalues.UniqueValues;

public class Function
{
	private String name;
	private List<String> params = new LinkedList<>();
	private List<Statement> statements = new LinkedList<>();
	private String returnType = "VOID";
	private UniqueValues searchSpace = new UniqueValues();
	private boolean securityInvoker;
	private String comment;

	public Function()
	{
	}
	public Function(String name, Collection<String> params, Collection<Statement> statements)
	{
		this.setName(name);
		this.extendArgs(params);
		this.extendStatements(statements);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<String> getParams()
	{
		return params;
	}

	public void extendArgs(Collection<String> params)
	{
		params
			.stream()
			.forEach(x -> this.extendArgs(x))
		;
	}
	public void extendArgs(String param)
	{
		this.params.add(param);
	}

	public List<Statement> getStatements()
	{
		return statements;
	}

	public void extendStatements(Collection<Statement> statements)
	{
		statements
			.stream()
			.forEach(x -> this.extendStatements(x))
		;
	}
	public void extendStatements(Statement stmt)
	{
		this.statements.add(stmt);
	}

	public String getReturnType()
	{
		return returnType;
	}
	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}

	public UniqueValues getSearchSpace()
	{
		return this.searchSpace;
	}

	public void extendSearchSpace(String schema)
	{
		this.searchSpace.addValue(schema);
	}

	public void removeSearchSpace(String schema)
	{
		this.searchSpace.removeValue(schema);
	}

	public void setSecurityInvoker(boolean b)
	{
		this.securityInvoker = b;
	}

	public boolean getSecurityInvoker()
	{
		return this.securityInvoker;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Function))
		{
			return super.equals(obj);
		}
		Function other = (Function) obj;
		return true
			&& this.name.equals(other.name)
			&& this.params.equals(other.params)
			&& this.statements.equals(other.statements)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.name,
			this.params,
			this.statements
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append("function ")
			.append(this.name)
			.append(" params = ")
			.append(this.params)
			.append(" statements = ")
			.append(this.statements)
			.toString()
		;
	}
}
