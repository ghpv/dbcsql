package ee.taltech.dbcsql.core.model.dsl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.argument.ArgumentDef;
import ee.taltech.dbcsql.core.model.dsl.argument.resolution.ArgumentPostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.argument.resolution.ArgumentPreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.argument.resolution.ArgumentResolver;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class ContractDef
{
	private String name = "";
	private List<ArgumentDef> arguments = new LinkedList<>();
	private List<PreconditionDef> preconditions = new LinkedList<>();
	private List<PostconditionDef> postconditions = new LinkedList<>();

	public ContractDef()
	{
	}

	public ContractDef(
		String name,
		List<ArgumentDef> arguments,
		List<PreconditionDef> preconditions,
		List<PostconditionDef> postconditions
	) {
		this.setName(name);
		this.setArguments(arguments);
		this.setPreconditions(preconditions);
		this.setPostconditions(postconditions);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setArguments(Collection<ArgumentDef> arguments)
	{
		arguments.forEach(x -> this.addArgument(x));
	}

	public void addArgument(ArgumentDef argument)
	{
		this.arguments.add(argument);
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
	public List<ArgumentDef> getArguments()
	{
		return arguments;
	}
	public List<PreconditionDef> getPreconditions()
	{
		return preconditions;
	}
	public List<PostconditionDef> getPostconditions()
	{
		return postconditions;
	}

	public void resolveArgumentTypes()
	{
		ArgumentResolver argRes = new ArgumentResolver();
		for (ArgumentDef arg: this.arguments)
		{
			argRes.addArgumentReference(arg);
		}

		ArgumentPreconditionVisitor preconditionVisitor = new ArgumentPreconditionVisitor(argRes);
		for (PreconditionDef precond: this.preconditions)
		{
			precond.accept(preconditionVisitor);
		}

		ArgumentPostconditionVisitor postconditionVisitor = new ArgumentPostconditionVisitor(argRes);
		for (PostconditionDef precond: this.postconditions)
		{
			precond.accept(postconditionVisitor);
		}

		if (!argRes.isComplete())
		{
			throw new TranslatorInputException("Argument type could not be resolved for: " + argRes.getUnresolvedArguments());
		}
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
			&& this.arguments.equals(other.arguments)
			&& this.preconditions.equals(other.preconditions)
			&& this.postconditions.equals(other.postconditions)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.name,
			this.arguments,
			this.preconditions,
			this.postconditions
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder("operation ")
			.append(name)
			.append("; args =")
			.append(arguments)
			.append("; preco =")
			.append(this.preconditions)
			.append("; postco = ")
			.append(this.postconditions)
			.toString()
		;
	}
}
