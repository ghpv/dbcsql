package ee.taltech.dbcsql.core.model.db;

public class TableDefBuilderBase<BuilderT extends TableDefBuilderBase<BuilderT>>
{
	protected TableDef data = new TableDef();

	@SuppressWarnings("unchecked")
	public BuilderT withName(DSLName dslName, DBName dbName)
	{
		this.data.setName(dslName);
		this.data.setName(dbName);
		return (BuilderT) this;
	}

	public BuilderT withName(String dslName, String dbName)
	{
		return this.withName(new DSLName(dslName), new DBName(dbName));
	}

	public BuilderT withName(String name)
	{
		return this.withName(name, name);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withColumn(String dslName, String dbName, String type)
	{
		ColumnDef colDef = new ColumnDef(data, dslName, dbName, type);
		this.data.addColumn(colDef);
		return (BuilderT) this;
	}

	public BuilderT withColumn(String name, String type)
	{
		return this.withColumn(name, name, type);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withParentTable(TableDef td)
	{
		this.data = td.clone();
		return (BuilderT) this;
	}
}
