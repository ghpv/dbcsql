package ee.taltech.dbcsql.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;

public abstract class DBDriver
{
	private Connection conn;
	private ConnectionDetails defaultConnectionDetails;

	public abstract TargetPlatform getTargetPlatform();

	public DBDriver(ConnectionDetails details)
	{
		this.loadDriver();
		this.defaultConnectionDetails = details;
		this.conn = this.newConnection();
	}
	protected void loadDriver()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected abstract String implementationDriverName();
	public abstract DatabaseDef readDb();

	public Connection newConnection()
	{
		return this.newConnection(this.defaultConnectionDetails);
	}
	public Connection newConnection(ConnectionDetails details)
	{
		try
		{
			String url = details.getConnectionUrl();
			return DriverManager.getConnection(url, details.getUsername(), details.getPassword());
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Connection connection()
	{
		return this.conn;
	}

	public DBDriver executeUpdate(String query, Object... args)
	{
		try
		(
			PreparedStatement ps = prepareStatement(query, args);
		)
		{
			ps.executeUpdate();
			return this;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public DBDriver execute(String query, Object... args)
	{
		try
		(
			PreparedStatement ps = prepareStatement(query, args);
		)
		{
			ps.execute();
			return this;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public PreparedStatement prepareStatement(String query, Object... args) throws SQLException
	{
		PreparedStatement ps = conn.prepareStatement(query);
		for (int idx = 0; idx < args.length; ++idx)
		{
			ps.setObject(idx+1, args[idx]);
		}
		return ps;
	}
	public void close()
	{
		try
		{
			this.conn.close();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
