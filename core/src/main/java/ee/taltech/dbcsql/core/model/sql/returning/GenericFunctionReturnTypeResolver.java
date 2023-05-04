package ee.taltech.dbcsql.core.model.sql.returning;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;

public class GenericFunctionReturnTypeResolver extends FunctionReturnTypeResolver
{
	public GenericFunctionReturnTypeResolver(ContractDef contract)
	{
		this
			.addPattern(new FunctionReturnTypeVoidPattern())
			.addPattern(new FunctionReturnTypeFromColumnPattern())
			.addPattern(new FunctionReturnTypeIntegerPattern())
			.addPattern(new FunctionReturnTypeVarcharPattern())
			.addPattern(new FunctionReturnTypeParameterPattern(contract.getParameters()))
		;
	}
}
