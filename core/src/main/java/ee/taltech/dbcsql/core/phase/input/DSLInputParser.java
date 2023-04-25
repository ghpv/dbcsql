package ee.taltech.dbcsql.core.phase.input;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public abstract class DSLInputParser <T>
{
	public T parse(InputStream input)
	{
		try
		{
			return this.parseThrows(input);
		}
		catch (IOException e)
		{
			throw new RuntimeException();
		}
	}

	private T parseThrows(InputStream input) throws IOException
	{
		CharStream charStream = CharStreams.fromStream(input);
		Lexer lexer = this.getLexer(charStream);
		lexer.removeErrorListeners();
		lexer.addErrorListener(DSLErrorListener.INSTANCE);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Parser parser = this.getParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(DSLErrorListener.INSTANCE);
		ParseTree tree = this.getTree(parser);

		ParseTreeWalker walker = new ParseTreeWalker();
		DSLListener<T> dataListener = this.getListener();
		walker.walk(dataListener, tree);
		return dataListener.getData();
	}

	protected abstract DSLListener<T> getListener();
	protected abstract Lexer getLexer(CharStream stream);
	protected abstract Parser getParser(CommonTokenStream stream);
	protected abstract ParseTree getTree(Parser parser);
}
