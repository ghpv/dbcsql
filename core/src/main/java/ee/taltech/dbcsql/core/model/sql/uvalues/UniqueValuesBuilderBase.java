package ee.taltech.dbcsql.core.model.sql.uvalues;

public class UniqueValuesBuilderBase<BuilderT extends UniqueValuesBuilderBase<BuilderT>>
{
	protected UniqueValues data = new UniqueValues();

	@SuppressWarnings("unchecked")
	public BuilderT add(String sym)
	{
		this.data.addValue(sym);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT remove(String sym)
	{
		this.data.removeValue(sym);
		return (BuilderT) this;
	}
}
