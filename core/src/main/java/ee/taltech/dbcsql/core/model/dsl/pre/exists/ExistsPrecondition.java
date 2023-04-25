package ee.taltech.dbcsql.core.model.dsl.pre.exists;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ExistsPrecondition implements PreconditionDef
{
	private VariableDef target;
	private RestrictionDef restrictions = new RestrictionDef();

	public ExistsPrecondition()
	{
	}

	public ExistsPrecondition(VariableDef target, RestrictionDef restrictions)
	{
		this.setTarget(target);
		this.setRestrictions(restrictions);
	}

	public VariableDef getTarget()
	{
		return target;
	}

	public RestrictionDef getRestrictions()
	{
		return restrictions;
	}

	public void setTarget(VariableDef target)
	{
		this.target = target;
	}

	public void setRestrictions(RestrictionDef restrictions)
	{
		this.restrictions = restrictions;
	}

	public void extendRestriction(RestrictionDef res)
	{
		this.restrictions.extendWith(res);
	}

	public void extendRestriction(Restriction res)
	{
		this.restrictions.extendWith(res);
	}

	@Override
	public <T> T accept(PreconditionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ExistsPrecondition))
		{
			return super.equals(obj);
		}
		ExistsPrecondition other = (ExistsPrecondition) obj;
		return true
			&& this.target.equals(other.target)
			&& this.restrictions.equals(other.restrictions)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.target,
			this.restrictions
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("exists ")
			.append(target.getTable().getAliasedName().getDSLName())
			.append(" ")
			.append(restrictions)
			.toString()
		;
	}
}
