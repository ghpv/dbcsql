package ee.taltech.dbcsql.core.phase.input.contract;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.dsl.ContractDefBuilder;
import ee.taltech.dbcsql.core.model.dsl.ContractDefBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionNodeCompositor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNodeBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeCompositor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNodeBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNodeBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNodeBuilder;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDefBuilder;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableRegistry;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;
import ee.taltech.dbcsql.core.phase.input.ContractBaseListener;
import ee.taltech.dbcsql.core.phase.input.ContractParser.ArgContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.CommentDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.ConnectionPreconditionContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.ContractContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.DeletedPostconditionContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.ExistsPreconditionContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.FunctionLiteralContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.HeaderDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.IdListContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.InsertedPostconditionContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.LiteralOrVarContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.LiteralOrVarListContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.PostconditionLinkContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.PostconditionUnlinkContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.PostconditionValueContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.RestrictionAtomMultiContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.RestrictionAtomSingleContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.RestrictionExprAndContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.RestrictionExprNegateContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.RestrictionExprOrContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.ReturnClauseContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.UpdatedPostconditionContext;
import ee.taltech.dbcsql.core.phase.input.ContractParser.VariableDefContext;
import ee.taltech.dbcsql.core.phase.input.DSLListener;
import ee.taltech.dbcsql.core.phase.input.NameMender;

public class ContractListener extends ContractBaseListener implements DSLListener<ContractDef>
{
	private ContractDefBuilder contract;
	private VariableDef currentRestrictionVariable;
	private VariableRegistry varReg;
	private DatabaseDef dbDef;
	private NameMender argumentNameMender;
	private NameMender functionNameMender;
	private Set<String> seenArguments = new HashSet<>();

	private Stack<ExpressionNode> nodeStack = new Stack<>();
	private ContractDefBuilderBase<ContractDefBuilder>.InsertedPostconditionSubBuilder inserted;
	private ContractDefBuilderBase<ContractDefBuilder>.UpdatedPostconditionSubBuilder updated;

	public ContractListener(GenerationContext context)
	{
		this.varReg = context.getVars();
		this.dbDef = context.getDatabaseDef();
		this.argumentNameMender = context.getArgumentMender();
		this.functionNameMender = context.getFunctionMender();
	}

	private String fixArgumentName(String name)
	{
		return this.argumentNameMender.mendName(name);
	}
	private String fixFunctionName(String name)
	{
		return this.functionNameMender.mendName(name);
	}
	private ComparisonTargetNode processLiteralOrVar(LiteralOrVarContext name)
	{
		// Function case
		if (name.functionLiteral() != null)
		{
			return this.processFunction(name.functionLiteral());
		}

		List<LiteralOrVarContext> subLiterals = name.literalOrVar();
		// Literal
		if (subLiterals.isEmpty())
		{
			return new LiteralComparisonTargetNodeBuilder()
				.withSymbol(this.fixLiteralOrVarNonComposite(name))
				.build();
		}
		// bracketed, separate composite call
		if (subLiterals.size() == 1)
		{
			return this.processLiteralOrVar(subLiterals.get(0));
		}

		// composite case
		assert subLiterals.size() == 2: "literalOrVar must form a binary tree";
		LiteralOrVarContext leftLiteral = subLiterals.get(0);
		LiteralOrVarContext rightLiteral = subLiterals.get(1);
		ComparisonTargetOperation op = ComparisonTargetOperation.fromSymbol(name.operator.getText());

		ComparisonTargetNode leftNode = this.processLiteralOrVar(leftLiteral);
		ComparisonTargetNode rightNode = this.processLiteralOrVar(rightLiteral);

		ComparisonTargetNodeCompositor compositor = new ComparisonTargetNodeCompositor();
		compositor.setOperation(op);
		leftNode.accept(compositor);
		ComparisonTargetNode ret = rightNode.accept(compositor);
		return ret;
	}

	private ComparisonTargetNode processFunction(FunctionLiteralContext functionLiteral)
	{
		List<ComparisonTargetNode> args = functionLiteral
			.literalOrVar()
			.stream()
			.map(x -> this.processLiteralOrVar(x))
			.collect(Collectors.toList())
		;
		return new FunctionComparisonTargetNodeBuilder()
			.withFunction(functionLiteral.ID().getText())
			.withArguments(args)
			.build()
		;
	}

	private String fixLiteralOrVarNonComposite(LiteralOrVarContext name)
	{
		if (name.symbol == null)
		{
			return name.getText();
		}

		this.argumentNameMender.setVerifySQLKeywords(false);
		String mendedName = this.argumentNameMender.mendName(name.symbol.getText());
		this.argumentNameMender.setVerifySQLKeywords(true);
		return mendedName;
	}

	@Override
	public void enterContract(ContractContext ctx)
	{
		this.contract = new ContractDefBuilder();
	}

	@Override
	public void enterHeaderDecl(HeaderDeclContext ctx)
	{
		this.contract.withName(this.fixFunctionName(ctx.func_name.getText()));
	}

	@Override
	public void enterArg(ArgContext ctx)
	{
		String argumentAlias = this.fixArgumentName(ctx.alias.getText());
		if (!seenArguments.add(argumentAlias))
		{
			throw new TranslatorInputException("Argument '" + argumentAlias + "' was already given!");
		}
		contract
			.makeArgument()
			.withAlias(argumentAlias)
			.build();
		;
	}

	@Override
	public void exitRestrictionAtomMulti(RestrictionAtomMultiContext ctx)
	{
		this.registerComparison(
			ctx.column.getText(),
			ctx.comp.getText(),
			this.splitTargetList(ctx.literalOrVarList())
		);
	}

	@Override
	public void exitRestrictionAtomSingle(RestrictionAtomSingleContext ctx)
	{
		this.registerComparison(
			ctx.column.getText(),
			ctx.comp.getText(),
			List.of(this.processLiteralOrVar(ctx.literalOrVar()))
		);
	}

	private void registerComparison(String columnName, String comparisonSymbol, List<ComparisonTargetNode> targets)
	{
		if (!this.currentRestrictionVariable.getTable().hasColumnDSL(columnName))
		{
			throw new TranslatorInputException(this.currentRestrictionVariable + " does not have column '" + columnName + "'!");
		}
		ColumnDef column = this
			.currentRestrictionVariable
			.getTable()
			.getColumnDSL(columnName);

		ComparisonType type = ComparisonType.fromSymbol(comparisonSymbol);
		assert type != null: "ComparisonType for " + comparisonSymbol + " was not found";

		this.nodeStack.push(
			new ComparisonExpressionNodeBuilder()
				.withColumn(this.currentRestrictionVariable, column.getAliasedName())
				.withType(type)
				.withTargets(targets)
			.build()
		);
	}

	@Override
	public void exitRestrictionExprNegate(RestrictionExprNegateContext ctx)
	{
		ExpressionNode node = this.nodeStack.pop();
		this.nodeStack.push(
			new UnaryExpressionNodeBuilder()
				.withNegate()
				.withNode(node)
			.build()
		);
	}

	@Override
	public void exitRestrictionExprAnd(RestrictionExprAndContext ctx)
	{
		this.compositeNodes(BooleanOperation.AND);
	}

	@Override
	public void exitRestrictionExprOr(RestrictionExprOrContext ctx)
	{
		this.compositeNodes(BooleanOperation.OR);
	}

	private void compositeNodes(BooleanOperation op)
	{
		ExpressionNode b = this.nodeStack.pop();
		ExpressionNode a = this.nodeStack.pop();
		ExpressionNodeCompositor compositor = new ExpressionNodeCompositor();
		compositor.setOperation(op);
		a.accept(compositor);
		ExpressionNode composite = b.accept(compositor);
		this.nodeStack.push(composite);
	}

	private List<ComparisonTargetNode> splitTargetList(LiteralOrVarListContext ctx)
	{
		return ctx
			.children
			.stream()
			.filter(x -> x instanceof LiteralOrVarContext)
			.map(x -> (LiteralOrVarContext) x)
			.map(x -> processLiteralOrVar(x))
			.collect(Collectors.toList())
		;
	}

	@Override
	public void enterExistsPrecondition(ExistsPreconditionContext ctx)
	{
		this.currentRestrictionVariable = this.makeVariable(
			ctx.variableDef()
		);
	}

	@Override
	public void exitExistsPrecondition(ExistsPreconditionContext ctx)
	{
		var b = this
			.contract
			.makeExistsPrecondition()
			.withTarget(this.currentRestrictionVariable)
		;
		if (!this.nodeStack.isEmpty())
		{
			b
				.makeRestriction()
					.makeExpressionRestriction()
						.withTarget(this.currentRestrictionVariable)
						.withNode(this.nodeStack.pop())
					.build()
				.build()
			;
			assert this.nodeStack.isEmpty(): "For some reason there are remaining nodes: " + this.nodeStack;
		}
		b.build();
	}

	@Override
	public void exitConnectionPrecondition(ConnectionPreconditionContext ctx)
	{
		this
			.contract
			.makeConnectionPrecondition()
				.betweenVariables(
					this.findExistingVariable(ctx.var1.getText()),
					this.findExistingVariable(ctx.var2.getText())
				)
				.withName(getNames(ctx.name))
			.build()
		;
	}

	private List<String> getNames(IdListContext ctx)
	{
		List<String> ret = new LinkedList<>();
		if (ctx == null)
		{
			return ret;
		}
		for (int i = 0; i < ctx.getChildCount(); ++i)
		{
			ret.add(ctx.getChild(i).getText());
		}
		return ret;
	}

	@Override
	public void exitDeletedPostcondition(DeletedPostconditionContext ctx)
	{
		VariableDef target = this.findExistingVariable(ctx.alias.getText());
		var builder = this
			.contract
			.makeDeletedPostcondition()
			.withTarget(target)
		;
		if (ctx.ret != null)
		{
			builder.withReturnValue(this.parseReturnValue(target, ctx.ret));
		}
		builder.build();
	}

	private ReturnValue parseReturnValue(VariableDef target, ReturnClauseContext ret)
	{
		ReturnValue rv = new ReturnValue();

		String returnSymbol = ret.literalOrVar().getText();
		rv.setSymbolValue(returnSymbol);

		TableDef table = target.getTable();
		if (table.hasColumnDSL(returnSymbol))
		{
			ColumnDef col = table.getColumnDSL(returnSymbol);
			rv.setColumn(col);
		}

		return rv;
	}

	@Override
	public void enterInsertedPostcondition(InsertedPostconditionContext ctx)
	{
		VariableDef target = this.makeVariable(ctx.variableDef());
		this.inserted = this
			.contract
			.makeInsertedPostcondition()
			.withTarget(target)
		;
		if (ctx.ret != null)
		{
			this.inserted.withReturnValue(this.parseReturnValue(target, ctx.ret));
		}
	}

	@Override
	public void enterUpdatedPostcondition(UpdatedPostconditionContext ctx)
	{
		VariableDef target = this.findExistingVariable(ctx.alias.getText());
		this.updated = this
			.contract
			.makeUpdatedPostcondition()
			.withTarget(target)
		;
		if (ctx.ret != null)
		{
			this.updated.withReturnValue(this.parseReturnValue(target, ctx.ret));
		}
	}

	@Override
	public void exitPostconditionValue(PostconditionValueContext ctx)
	{
		ComparisonTargetNode val = this.processLiteralOrVar(ctx.value);
		if (this.inserted != null)
		{
			this.inserted.addValue(
				ctx.dsl_col_name.getText(),
				val
			);
		}
		else if (this.updated != null)
		{
			this.updated.addValue(
				ctx.dsl_col_name.getText(),
				val
			);
		}
		else
		{
			throw new RuntimeException("No context to add value to for: " + ctx.getText());
		}
	}

	@Override
	public void exitPostconditionLink(PostconditionLinkContext ctx)
	{
		VariableDef var = this.findExistingVariable(ctx.var.getText());
		List<String> names = this.getNames(ctx.name);

		if (this.inserted != null)
		{
			this.inserted.linkWith(var, names);
		}
		else if (this.updated != null)
		{
			this.updated.linkWith(var, names);
		}
		else
		{
			throw new RuntimeException("No context to link with for: " + ctx.getText());
		}
	}

	@Override
	public void exitPostconditionUnlink(PostconditionUnlinkContext ctx)
	{
		VariableDef var = this.findExistingVariable(ctx.var.getText());
		List<String> names = this.getNames(ctx.name);

		if (this.inserted != null)
		{
			throw new RuntimeException("Unlink is not available for inserted!");
		}
		else if (this.updated != null)
		{
			this.updated.unlinkFrom(var, names);
		}
		else
		{
			throw new RuntimeException("No context to link with for: " + ctx.getText());
		}
	}

	@Override
	public void exitInsertedPostcondition(InsertedPostconditionContext ctx)
	{
		this.inserted.build();
		this.inserted = null;
	}

	@Override
	public void exitUpdatedPostcondition(UpdatedPostconditionContext ctx)
	{
		this.updated.build();
		this.updated = null;
	}

	@Override
	public void enterCommentDecl(CommentDeclContext ctx)
	{
		this.contract.withComment(ctx.STRING().getText());
	}

	private VariableDef findVariable(String alias)
	{
		return this
			.varReg
			.find(alias);
	}

	private VariableDef findExistingVariable(String alias)
	{
		VariableDef ret = this.findVariable(alias);
		if (ret == null)
		{
			throw new TranslatorInputException("Did not find variable '" + alias + "'!");
		}
		return ret;
	}

	private VariableDef makeVariable(VariableDefContext ctx)
	{
		return this.makeVariable(
			ctx.table.getText(),
			ctx.alias.getText()
		);
	}
	private VariableDef makeVariable(String table, String alias)
	{
		if (this.findVariable(alias) != null)
		{
			throw new TranslatorInputException("Variable for alias '" + alias + "' exists already!");
		}
		TableDef tableDef = this.dbDef.getTableDSL(table);
		if (tableDef == null)
		{
			throw new TranslatorInputException("Did not find table '" + table + "'!");
		}
		this
			.varReg
			.registerVariable(new VariableDefBuilder()
				.withAlias(alias)
				.withTable(tableDef)
				.build()
			);
		return this.findVariable(alias);
	}

	@Override
	public ContractDef getData()
	{
		return this.contract.build();
	}
}
