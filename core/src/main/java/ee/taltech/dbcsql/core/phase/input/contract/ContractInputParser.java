package ee.taltech.dbcsql.core.phase.input.contract;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.input.ContractLexer;
import ee.taltech.dbcsql.core.phase.input.ContractParser;
import ee.taltech.dbcsql.core.phase.input.DSLInputParser;
import ee.taltech.dbcsql.core.phase.input.DSLListener;

public class ContractInputParser extends DSLInputParser<ContractDef>
{
	GenerationContext context;
	public ContractInputParser(GenerationContext context)
	{
		this.context = context;
	}

	@Override
	protected DSLListener<ContractDef> getListener()
	{
		return new ContractListener(this.context);
	}

	@Override
	protected Lexer getLexer(CharStream stream)
	{
		return new ContractLexer(stream);
	}

	@Override
	protected Parser getParser(CommonTokenStream stream)
	{
		return new ContractParser(stream);
	}

	@Override
	protected ParseTree getTree(Parser parser)
	{
		return ((ContractParser) parser).init();
	}
}
