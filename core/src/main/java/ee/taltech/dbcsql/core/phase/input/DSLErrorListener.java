package ee.taltech.dbcsql.core.phase.input;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class DSLErrorListener extends BaseErrorListener
{
	public static final DSLErrorListener INSTANCE = new DSLErrorListener();

	@Override
	public void syntaxError(
		Recognizer<?, ?> recognizer,
		Object offendingSymbol,
		int line,
		int charPositionInLine,
		String msg,
		RecognitionException e
	) throws
		ParseCancellationException
	{
		throw new TranslatorInputException("line " + line + ":" + charPositionInLine + " " + msg);
	}
}
