package ee.taltech.dbcsql.test.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.db.postgres.PostgresDriver;

public class PostgresTestDriver extends PostgresDriver
{
	private static final String RESET_SCRIPT = "uc/reset.sql";
	public PostgresTestDriver()
	{
		super();
		this.setupDatabase();
	}

	protected void setupDatabase()
	{
		this.executeResource(RESET_SCRIPT);
	}

	public void executeResource(String string)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(this
				.getClass()
				.getClassLoader()
				.getResourceAsStream(string)));

			StringBuilder sql = new StringBuilder();
			List<String> queries = new LinkedList<>();
			while (reader.ready())
			{
				String line = reader.readLine();
				if (line.equals(";"))
				{
					queries.add(sql.toString());
					sql.setLength(0);
					continue;
				}
				sql
					.append(line)
					.append("\n")
				;
			}

			for (String qry: queries)
			{
				this.execute(qry);
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

}
