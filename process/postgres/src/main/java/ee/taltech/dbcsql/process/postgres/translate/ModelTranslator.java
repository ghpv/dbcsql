package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.dsl.argument.ArgumentDef;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.model.sql.Function;
import ee.taltech.dbcsql.core.model.sql.FunctionBuilder;
import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class ModelTranslator
{
	private GenerationContext context;

	public ModelTranslator(GenerationContext context)
	{
		this.context = context;
	}

	public Function translateContract(ContractDef contract)
	{
		FunctionBuilder func = new FunctionBuilder();
		func.withName(contract.getName());
		func.withSecurityInvoker(this.context.getSecurityInvoker());

		this
			.translateArguments(contract, func)
			.translatePostconditionsToStatementsAndResolveReturnType(contract, func)
			.resolveSearchPath(contract, func)
		;
		if (contract.getComment().isPresent())
		{
			String comm = contract
				.getComment()
				.get()
				.replaceAll("\\\\'", "''")
			;
			func.withComment(comm);
		}

		return func.build();
	}

	private ModelTranslator translateArguments(ContractDef contract, FunctionBuilder func)
	{
		contract
			.getArguments()
			.stream()
			.map(x -> translateArgument(x))
			.forEach(x -> func.addArgument(x))
		;
		return this;
	}
	private ModelTranslator translatePostconditionsToStatementsAndResolveReturnType(ContractDef contract, FunctionBuilder func)
	{
		this.handleReturnLastPostconditionOption(contract);
		PostgresStatementReturnTypeResolver returnTypeResolver = new PostgresStatementReturnTypeResolver(contract);
		List<PostconditionDef> processedPostconditions = new LinkedList<>();

		for (PostconditionDef post: contract.getPostconditions())
		{
			this.prepareRestrictionRegistry(
				contract.getPreconditions(),
				processedPostconditions,
				post
			);
			Statement stmt = translatePostcondition(post);
			processedPostconditions.add(post);
			func.addStatement(stmt);
			String returnType = stmt.accept(returnTypeResolver);
			if (returnType != null)
			{
				func.withReturnType(returnType);
			}
		}
		return this;
	}

	private void handleReturnLastPostconditionOption(ContractDef contract)
	{
		if (!this.context.isReturnLastPostcondition())
		{
			return;
		}
		PostconditionDef lastPostcondition = contract.getPostconditions().get(contract.getPostconditions().size() - 1);
		lastPostcondition.accept(new ReturnIdentifierVisitor());
	}

	private ModelTranslator resolveSearchPath(ContractDef contract, FunctionBuilder func)
	{
		SearchPathResolverRestrictionVisitor spathResolver = new SearchPathResolverRestrictionVisitor(this.context, func::withSearchSpace);

		contract
			.getPreconditions()
			.stream()
			.forEach(x -> x.accept(spathResolver))
		;
		contract
			.getPostconditions()
			.stream()
			.forEach(x -> x.accept(spathResolver))
		;

		func
			.removeSearchSpace("public")
			.removeSearchSpace("pg_temp")
			.withSearchSpace("public")
			.withSearchSpace("pg_temp")
		;
		return this;
	}

	private String translateArgument(ArgumentDef x)
	{
		return new StringBuilder()
			.append(x.getAlias())
			.append(" ")
			.append(x.getType().toString())
			.toString()
		;
	}

	public Statement translatePostcondition(PostconditionDef postcondition)
	{
		PostconditionTranslator tr = new PostconditionTranslator(this.context);
		postcondition.accept(tr);
		return tr.getStatement();
	}

	private void prepareRestrictionRegistry(
		Collection<PreconditionDef> preconditions,
		Collection<PostconditionDef> seenPostconditions,
		PostconditionDef currentPostcondition
	) {
		this.context.getRestrictionRegistry().resetRestrictions();
		PostcondtionToRestrictionVisitor postVisitor = new PostcondtionToRestrictionVisitor(this.context);
		seenPostconditions
			.forEach(x -> x.accept(postVisitor))
		;
		postVisitor.apply();

		PrecondtionToRestrictionVisitor preVisitor = new PrecondtionToRestrictionVisitor(this.context);
		preconditions
			.forEach(x -> x.accept(preVisitor))
		;


		TargettedPostcondtionToRestrictionVisitor targetPostVisitor = new TargettedPostcondtionToRestrictionVisitor(this.context);
		currentPostcondition.accept(targetPostVisitor);
	}
}
