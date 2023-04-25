package ee.taltech.dbcsql.core.model.dsl.post.deleted;

import java.util.Objects;
import java.util.Optional;

import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class DeletedPostcondition implements PostconditionDef
{
	private VariableDef target;
	private Optional<ReturnValue> returnValue = Optional.empty();

	public DeletedPostcondition()
	{
	}

	public DeletedPostcondition(VariableDef target)
	{
		this.target = target;
	}

	@Override
	public <T> T accept(PostconditionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	public void setTarget(VariableDef var)
	{
		this.target = var;
	}

	public VariableDef getTarget()
	{
		return target;
	}

	public void setReturnValue(ReturnValue val)
	{
		this.returnValue = Optional.of(val);
	}

	public Optional<ReturnValue> getReturnValue()
	{
		return this.returnValue;
	}

	@Override
	public String toString()
	{
		return new StringBuilder("delete ")
			.append(target)
			.toString()
		;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DeletedPostcondition))
		{
			return super.equals(obj);
		}
		DeletedPostcondition other = (DeletedPostcondition) obj;
		return true
			&& this.target.equals(other.target)
			&& this.returnValue.equals(other.returnValue)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.target,
			this.returnValue
		);
	}
}
