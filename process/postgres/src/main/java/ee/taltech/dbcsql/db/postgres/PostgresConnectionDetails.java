package ee.taltech.dbcsql.db.postgres;

import ee.taltech.dbcsql.core.db.ConnectionDetails;

public class PostgresConnectionDetails extends ConnectionDetails
{
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 5432;
	private static final String DEFAULT_DATABASE = "postgres";
	private static final String DEFAULT_USERNAME = DEFAULT_DATABASE;
	private static final String DEFAULT_PASSWORD = DEFAULT_DATABASE;

	public PostgresConnectionDetails(String url, int port, String database, String username, String password)
	{
		super(url, port, database, username, password);
	}

	public PostgresConnectionDetails()
	{
		super(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}

	@Override
	protected String getDbIdentifier()
	{
		return "postgresql";
	}
}
