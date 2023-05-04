package ee.taltech.dbcsql.core.phase;

import java.util.Collection;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.dsl.ContractDefBuilderBase;

public class GenerationRequestBuilder
{
	protected GenerationRequest data = new GenerationRequest();

	public GenerationRequestBuilder withContracts(Collection<ContractDef> contracts)
	{
		for (ContractDef c: contracts)
		{
			this.withContract(c);
		}
		return this;
	}

	public GenerationRequestBuilder withContracts(ContractDef... contracts)
	{
		return this.withContracts(List.of(contracts));
	}

	public GenerationRequestBuilder withContract(ContractDef contract)
	{
		contract.resolveParameterTypes();
		this.data.contracts.add(contract);
		return this;
	}

	public class ContractDefSubBuilder extends ContractDefBuilderBase<ContractDefSubBuilder>
	{
		private GenerationRequestBuilder owner;
		protected ContractDefSubBuilder(GenerationRequestBuilder owner)
		{
			this.owner = owner;
		}

		public GenerationRequestBuilder build()
		{
			this.owner.withContract(this.data);
			return this.owner;
		}
	}

	public ContractDefSubBuilder makeContract()
	{
		return new ContractDefSubBuilder(this);
	}

	public GenerationRequestBuilder withContext(GenerationContext context)
	{
		this.data.context = context;
		return this;
	}

	public GenerationContext getDataContext()
	{
		return this.data.context;
	}

	public GenerationRequest build()
	{
		return this.data;
	}
}
