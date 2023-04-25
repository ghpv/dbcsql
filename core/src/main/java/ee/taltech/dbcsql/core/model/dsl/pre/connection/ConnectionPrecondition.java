package ee.taltech.dbcsql.core.model.dsl.pre.connection;

import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ConnectionPrecondition implements PreconditionDef
{
	private VariableDef a;
	private VariableDef b;
	private List<String> names = new LinkedList<>();

	public ConnectionPrecondition()
	{
	}

	public ConnectionPrecondition(VariableDef a, VariableDef b)
	{
		this.setConnection(a, b);
	}

	public ConnectionPrecondition(VariableDef a, VariableDef b, String name)
	{
		this(a, b);
		this.addName(name);
	}

	public ConnectionPrecondition(VariableDef a, VariableDef b, List<String> name)
	{
		this(a, b);
		this.setNames(name);
	}

	public List<String> getNames()
	{
		return names;
	}

	public void addName(String name)
	{
		this.names.add(name);
	}

	public void setNames(List<String> names)
	{
		this.names = names;
	}

	public void setConnection(VariableDef a, VariableDef b)
	{
		this.a = a;
		this.b = b;
	}

	public VariableDef getA()
	{
		return a;
	}

	public VariableDef getB()
	{
		return b;
	}

	@Override
	public <T> T accept(PreconditionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("connection between ")
			.append(a)
			.append(" and ")
			.append(b)
			.toString()
		;
	}
}
