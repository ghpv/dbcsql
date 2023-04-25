package ee.taltech.dbcsql.core.model.dsl;

public class ContractDefBuilder extends ContractDefBuilderBase<ContractDefBuilder>
{
	public ContractDef build()
	{
		this.data.resolveArgumentTypes();
		return this.data;
	}
}
