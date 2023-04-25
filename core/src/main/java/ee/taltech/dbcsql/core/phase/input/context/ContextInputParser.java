package ee.taltech.dbcsql.core.phase.input.context;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.phase.input.ContextLexer;
import ee.taltech.dbcsql.core.phase.input.ContextParser;
import ee.taltech.dbcsql.core.phase.input.DSLInputParser;
import ee.taltech.dbcsql.core.phase.input.DSLListener;

public class ContextInputParser extends DSLInputParser<DatabaseDef>
{
	@Override
	protected DSLListener<DatabaseDef> getListener()
	{
		return new ContextListener();
	}

	@Override
	protected Lexer getLexer(CharStream stream)
	{
		return new ContextLexer(stream);
	}

	@Override
	protected Parser getParser(CommonTokenStream stream)
	{
		return new ContextParser(stream);
	}

	@Override
	protected ParseTree getTree(Parser parser)
	{
		return ((ContextParser) parser).init();
	}
}
