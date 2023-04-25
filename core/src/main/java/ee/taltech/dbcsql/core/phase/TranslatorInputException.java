package ee.taltech.dbcsql.core.phase;

public class TranslatorInputException extends RuntimeException
{
	public TranslatorInputException()
	{
	}

	public TranslatorInputException(String message)
	{
		super(message);
	}

	public TranslatorInputException(Throwable cause)
	{
		super(cause);
	}

	public TranslatorInputException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TranslatorInputException(
		String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace
	) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
