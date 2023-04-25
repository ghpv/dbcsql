package ee.taltech.dbcsql.core.model.dsl.restriction.connection;

public class ConnectionRestrictionBuilder extends ConnectionRestrictionBuilderBase<ConnectionRestrictionBuilder>
{
	public ConnectionRestriction build()
	{
		return this.data;
	}
}
