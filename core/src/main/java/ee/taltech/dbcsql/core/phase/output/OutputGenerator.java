package ee.taltech.dbcsql.core.phase.output;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.output.persistence.Persistence;

public abstract class OutputGenerator
{
	protected GenerationContext context;
	protected Persistence persistence;

	public OutputGenerator(GenerationContext context)
	{
		this.context = context;
		this.persistence = context.getPersistence();
	}

	public void generate(GenerationRequest request)
	{
		for (ContractDef contract: request.getContracts())
		{
			this.generateContract(contract);
		}
	}

	protected void generateContract(ContractDef contract)
	{
		this.persistence.startNew(contract.getName());
		this.generateContractImpl(contract);
		this.persistence.finished();
	}

	protected Persistence getPersistence()
	{
		return this.persistence;
	}

	protected abstract void generateContractImpl(ContractDef contract);
}
