package ee.taltech.dbcsql.core.phase.input;

import ee.taltech.dbcsql.core.phase.TranslatorInputException;

public class NameMenderException extends TranslatorInputException
{
	public NameMenderException()
	{
	}

	public NameMenderException(String message)
	{
		super(message);
	}

	public NameMenderException(Throwable cause)
	{
		super(cause);
	}

	public NameMenderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NameMenderException(
		String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace
	) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
