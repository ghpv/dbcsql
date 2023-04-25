package ee.taltech.dbcsql.core.model.dsl.restriction.connection;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ConnectionRestriction implements Restriction
{
	private VariableDef a;
	private VariableDef b;
	private List<String> names = new LinkedList<>();
	private boolean newLink = false;
	private boolean unlink = false;

	public ConnectionRestriction()
	{
	}

	public ConnectionRestriction(VariableDef a, VariableDef b, List<String> names, boolean newLink, boolean unlink)
	{
		this.setConnection(a, b);
		this.setNames(names);
		this.setNewLink(newLink);
		this.setUnlink(unlink);
	}

	public ConnectionRestriction clone()
	{
		return new ConnectionRestriction(this.getA(), this.getB(), this.getNames(), this.isNewLink(), this.isUnlink());
	}

	public void setConnection(VariableDef a, VariableDef b)
	{
		this.a = a;
		this.b = b;
	}

	public List<String> getNames()
	{
		return names;
	}

	public void setNames(List<String> names)
	{
		this.names = names;
	}

	public void addName(String name)
	{
		this.names.add(name);
	}

	public VariableDef getA()
	{
		return a;
	}

	public VariableDef getB()
	{
		return b;
	}

	public VariableDef getOther(VariableDef v)
	{
		if (this.getA().getAlias().equals(v.getAlias()))
		{
			return this.getB();
		}
		if (this.getB().getAlias().equals(v.getAlias()))
		{
			return this.getA();
		}
		return null;
	}

	public boolean contains(VariableDef v)
	{
		return a.getAlias().equals(v.getAlias())
			|| b.getAlias().equals(v.getAlias())
		;
	}

	public boolean isNewLink()
	{
		return newLink;
	}

	public ConnectionRestriction setNewLink(boolean val)
	{
		this.newLink = val;
		return this;
	}

	public boolean isUnlink()
	{
		return unlink;
	}

	public void setUnlink(boolean unlink)
	{
		this.unlink = unlink;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ConnectionRestriction))
		{
			return super.equals(obj);
		}
		ConnectionRestriction other = (ConnectionRestriction) obj;
		return true
			&& this.a.equals(other.a)
			&& this.b.equals(other.b)
			&& this.names.equals(other.names)
			&& this.newLink == other.newLink
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.a,
			this.b,
			this.names,
			this.newLink
		);
	}

	@Override
	public <T> T accept(RestrictionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.newLink ? "new" : "existing")
			.append(" ")
			.append(this.a)
			.append(" -- ")
			.append(this.b)
			.append(": ")
			.append(this.names)
			.toString()
		;
	}

	@Override
	public boolean canBeCombined()
	{
		return false;
	}

	@Override
	public boolean combineWith(Restriction other)
	{
		return false;
	}
}
