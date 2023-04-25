package ee.taltech.dbcsql.core.phase;

import java.util.LinkedList;
import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;

public class GenerationRequest
{
	protected List<ContractDef> contracts = new LinkedList<>();
	protected GenerationContext context;

	public List<ContractDef> getContracts()
	{
		return this.contracts;
	}
	public GenerationContext getContext()
	{
		return this.context;
	}

	@Override
	public String toString()
	{
		return this.contracts.toString();
	}
}
