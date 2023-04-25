package ee.taltech.dbcsql.core.model.db;

public class PKey
{
	private ColumnDef column;

	public PKey(ColumnDef col)
	{
		this.setColumn(col);
	}

	public ColumnDef getColumn()
	{
		return column;
	}

	public void setColumn(ColumnDef column)
	{
		this.column = column;
	}
}
