package ee.taltech.dbcsql.core.db;

public abstract class ConnectionDetails
{
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	private String connectionUrl;

	public ConnectionDetails(String url, int port, String database, String username, String password)
	{
		this.host = url;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.connectionUrl = this.buildConnectionUrl();
	}

	private String buildConnectionUrl()
	{
		return new StringBuilder("jdbc:")
			.append(this.getDbIdentifier())
			.append("://")
			.append(this.host)
			.append(":")
			.append(this.port)
			.append("/")
			.append(this.database)
			.toString()
		;
	}
	protected abstract String getDbIdentifier();

	public String getConnectionUrl()
	{
		return this.connectionUrl;
	}
	public String getUsername()
	{
		return this.username;
	}
	public String getPassword()
	{
		return this.password;
	}
	public String getHost()
	{
		return this.host;
	}
	public int getPort()
	{
		return this.port;
	}
	public String getDatabase()
	{
		return this.database;
	}
}
