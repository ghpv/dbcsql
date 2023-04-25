package ee.taltech.dbcsql.core.model.dsl.restriction;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class RestrictionDef
{
	private List<Restriction> restrictions = new LinkedList<>();

	public RestrictionDef()
	{
	}

	public RestrictionDef(List<Restriction> res)
	{
		this.setRestrictions(res);
	}

	public List<Restriction> getRestrictions()
	{
		return Collections.unmodifiableList(this.restrictions);
	}

	public void setRestrictions(List<Restriction> restrictions)
	{
		this.restrictions = restrictions;
	}

	public RestrictionDef extendWith(Collection<Restriction> res)
	{
		res
			.stream()
			.forEach(x -> this.extendWith(x))
		;
		return this;
	}

	public RestrictionDef extendWith(RestrictionDef res)
	{
		return this.extendWith(res.getRestrictions());
	}

	public RestrictionDef extendWith(Restriction res)
	{
		if (res.canBeCombined())
		{
			for (Restriction r: this.restrictions)
			{
				if (r.combineWith(res))
				{
					return this;
				}
			}
		}
		for (Restriction r: this.restrictions)
		{
			if (r.equals(res))
			{
				return this;
			}
		}
		this.restrictions.add(res);
		return this;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof RestrictionDef))
		{
			return super.equals(obj);
		}
		RestrictionDef other = (RestrictionDef) obj;
		return true
			&& this.restrictions.equals(other.restrictions)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.restrictions
		);
	}

	@Override
	public String toString()
	{
		return this.restrictions.toString();
	}

	public RestrictionDef clone()
	{
		RestrictionDef copy = new RestrictionDef();
		for (Restriction r: this.restrictions)
		{
			copy.extendWith(r.clone());
		}
		return copy;
	}
}
