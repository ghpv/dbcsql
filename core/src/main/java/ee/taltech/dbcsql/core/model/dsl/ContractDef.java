package ee.taltech.dbcsql.core.model.dsl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ee.taltech.dbcsql.core.model.dsl.parameter.ParameterDef;
import ee.taltech.dbcsql.core.model.dsl.parameter.resolution.ParameterPostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.parameter.resolution.ParameterPreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.parameter.resolution.ParameterResolver;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class ContractDef
{
	private String name = "";
	private List<ParameterDef> parameters = new LinkedList<>();
	private List<PreconditionDef> preconditions = new LinkedList<>();
	private List<PostconditionDef> postconditions = new LinkedList<>();
	private Optional<String> comment = Optional.empty();

	public ContractDef()
	{
	}

	public ContractDef(
		String name,
		List<ParameterDef> parameters,
		List<PreconditionDef> preconditions,
		List<PostconditionDef> postconditions
	) {
		this.setName(name);
		this.setParameters(parameters);
		this.setPreconditions(preconditions);
		this.setPostconditions(postconditions);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setParameters(Collection<ParameterDef> parameters)
	{
		parameters.forEach(x -> this.addParameter(x));
	}

	public void addParameter(ParameterDef parameter)
	{
		this.parameters.add(parameter);
	}

	public void setPreconditions(List<PreconditionDef> preconditions)
	{
		preconditions.forEach(x -> this.addPrecondition(x));
	}

	public void addPrecondition(PreconditionDef condition)
	{
		this.preconditions.add(condition);
	}

	public void setPostconditions(List<PostconditionDef> postconditions)
	{
		postconditions.forEach(x -> this.addPostcondition(x));
	}

	public void addPostcondition(PostconditionDef condition)
	{
		this.postconditions.add(condition);
	}

	public String getName()
	{
		return name;
	}
	public List<ParameterDef> getParameters()
	{
		return parameters;
	}
	public List<PreconditionDef> getPreconditions()
	{
		return preconditions;
	}
	public List<PostconditionDef> getPostconditions()
	{
		return postconditions;
	}

	public void resolveParameterTypes()
	{
		ParameterResolver paramRes = new ParameterResolver();
		for (ParameterDef param: this.parameters)
		{
			paramRes.addParameterReference(param);
		}

		ParameterPreconditionVisitor preconditionVisitor = new ParameterPreconditionVisitor(paramRes);
		for (PreconditionDef precond: this.preconditions)
		{
			precond.accept(preconditionVisitor);
		}

		ParameterPostconditionVisitor postconditionVisitor = new ParameterPostconditionVisitor(paramRes);
		for (PostconditionDef precond: this.postconditions)
		{
			precond.accept(postconditionVisitor);
		}

		if (!paramRes.isComplete())
		{
			throw new TranslatorInputException("Parameter type could not be resolved for: " + paramRes.getUnresolvedParameters());
		}
	}

	public Optional<String> getComment()
	{
		return comment;
	}

	public void setComment(Optional<String> comment)
	{
		this.comment = comment;
	}

	public void setComment(String comment)
	{
		this.setComment(Optional.ofNullable(comment));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ContractDef))
		{
			return super.equals(obj);
		}
		ContractDef other = (ContractDef) obj;
		return true
			&& this.name.equals(other.name)
			&& this.parameters.equals(other.parameters)
			&& this.preconditions.equals(other.preconditions)
			&& this.postconditions.equals(other.postconditions)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.name,
			this.parameters,
			this.preconditions,
			this.postconditions
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("operation ")
			.append(name)
			.append("; params =")
			.append(parameters)
			.append("; preco =")
			.append(this.preconditions)
			.append("; postco = ")
			.append(this.postconditions)
			.toString()
		;
	}
}
