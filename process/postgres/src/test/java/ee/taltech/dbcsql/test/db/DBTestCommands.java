package ee.taltech.dbcsql.test.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.db.DBCommands;

public class DBTestCommands extends DBCommands
{
	private static final String TEST_DATABASE = "uc/database.sql";
	private static final String SAMPLE_DATA_DATABASE = "sdt/context.sql";

	public DBTestCommands(PostgresTestDriver driver)
	{
		super(driver);
	}
	public DBTestCommands()
	{
		super(new PostgresTestDriver());
	}

	public Set<String> getUserTables()
	{
		return this.singleColumnQuery(String.class, DBQueries.USER_TABLES);
	}

	public Set<?> getUserFunctions()
	{
		return this.singleColumnQuery(String.class, DBQueries.USER_FUNCTIONS);
	}

	private <T> Set<T> singleColumnQuery(Class<T> castType, String query, Object... args)
	{
		return this
			.singleColumnQuery(query, args)
			.stream()
			.map(x -> castType.cast(x))
			.collect(Collectors.toSet())
		;
	}

	private Set<Object> singleColumnQuery(String query, Object... args)
	{
		try
		(
			PreparedStatement ps = this.driver.prepareStatement(query, args);
			ResultSet rs = ps.executeQuery();
		)
		{
			Set<Object> ret = new HashSet<>();
			while (rs.next())
			{
				String value = rs.getString(1);
				ret.add(value);
			}
			return ret;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public List<List<Object>> readQuery(String query, Object... args)
	{
		try
		(
			PreparedStatement ps = this.driver.prepareStatement(query, args);
			ResultSet rs = ps.executeQuery();
		)
		{
			List<List<Object>> ret = new LinkedList<>();
			int colCnt = rs.getMetaData().getColumnCount();
			while (rs.next())
			{
				List<Object> tmp = new LinkedList<>();
				for (int i = 1; i <= colCnt; ++i)
				{
					tmp.add(rs.getObject(i));
				}
				ret.add(tmp);
			}
			return ret;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void loadTestDatabase()
	{
		this.actualDriver().executeResource(TEST_DATABASE);
	}

	public void loadSampleDataDatabase()
	{
		this.actualDriver().executeResource(SAMPLE_DATA_DATABASE);
	}

	public PostgresTestDriver actualDriver()
	{
		return (PostgresTestDriver) this.driver;
	}
}
