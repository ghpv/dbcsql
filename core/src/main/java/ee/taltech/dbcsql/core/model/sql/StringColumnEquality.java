package ee.taltech.dbcsql.core.model.sql;

import ee.taltech.dbcsql.core.model.Pair;
import ee.taltech.dbcsql.core.model.db.ColumnDef;

public class StringColumnEquality extends Pair<String, String>
{
	public StringColumnEquality(String key, String value)
	{
		super(key, value);
	}

	public StringColumnEquality(ColumnDef col, String value)
	{
		this(
			col
				.getAliasedName()
				.getDBName()
				.getName()
			,
			value
		);
	}
}
