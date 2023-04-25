package ee.taltech.dbcsql.core.model.dsl.post.updated;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.post.VariableLink;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class UpdatedPostcondition implements PostconditionDef
{
	private VariableDef target;
	private List<ColumnEquality> values = new LinkedList<>();
	private List<VariableLink> linkedVariables = new LinkedList<>();
	private Optional<ReturnValue> returnValue = Optional.empty();

	public UpdatedPostcondition()
	{
	}

	public UpdatedPostcondition(VariableDef target)
	{
		this.target = target;
	}

	@Override
	public <T> T accept(PostconditionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	public UpdatedPostcondition setTarget(VariableDef var)
	{
		this.target = var;
		return this;
	}

	public VariableDef getTarget()
	{
		return target;
	}

	public UpdatedPostcondition addValue(String dslName, ComparisonTargetNode val)
	{
		if (!this.target.getTable().hasColumnDSL(dslName))
		{
			throw new TranslatorInputException("Did not find column '" + dslName + "' for variable " + this.target);
		}
		ColumnDef col = this
			.target
			.getTable()
			.getColumnDSL(dslName)
		;
		return this.addValue(col, val);
	}

	public UpdatedPostcondition addValue(ColumnDef col, ComparisonTargetNode val)
	{
		return this.addValue(new ColumnEquality(col, val));
	}

	public UpdatedPostcondition addValue(ColumnEquality val)
	{
		this.values.add(val);
		return this;
	}

	public List<ColumnEquality> getValues()
	{
		return this.values;
	}

	public UpdatedPostcondition linkWith(VariableDef var, List<String> names)
	{
		return this.linkWith(new VariableLink(var, names));
	}

	public UpdatedPostcondition linkWith(VariableLink varLink)
	{
		this.linkedVariables.add(varLink);
		return this;
	}

	public void unlinkFrom(VariableDef var, List<String> names)
	{
		VariableLink link = new VariableLink(var, names);
		link.setUnlink(true);
		this.linkWith(link);
	}

	public List<VariableLink> getLinks()
	{
		return this.linkedVariables;
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
		return new StringBuilder("update ")
			.append(target)
			.append(values)
			.toString()
		;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof UpdatedPostcondition))
		{
			return super.equals(obj);
		}
		UpdatedPostcondition other = (UpdatedPostcondition) obj;
		return true
			&& this.target.equals(other.target)
			&& this.values.equals(other.values)
			&& this.linkedVariables.equals(other.linkedVariables)
			&& this.returnValue.equals(other.returnValue)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.target,
			this.values,
			this.linkedVariables,
			this.returnValue
		);
	}
}
