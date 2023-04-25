package ee.taltech.dbcsql.core.model.db;

public class ConnectedStrings extends ConnectedValues<String>
{
	public ConnectedStrings(String a, String b)
	{
		super(a, b);
	}

	@Override
	protected int compare(String a, String b)
	{
		return a.compareTo(b);
	}
}
