package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.sql.StatementVisitor;
import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatement;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatement;
import ee.taltech.dbcsql.core.model.sql.returning.FunctionReturnTypeResolver;
import ee.taltech.dbcsql.core.model.sql.returning.GenericFunctionReturnTypeResolver;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatement;

public class PostgresStatementReturnTypeResolver implements StatementVisitor<String>
{
	private FunctionReturnTypeResolver resolver;

	public PostgresStatementReturnTypeResolver(ContractDef contract)
	{
		this.resolver = new GenericFunctionReturnTypeResolver(contract);
	}

	@Override
	public String visit(DeleteStatement v)
	{
		return resolver.resolveReturnType(v.getReturnValue());
	}

	@Override
	public String visit(InsertStatement v)
	{
		return resolver.resolveReturnType(v.getReturnValue());
	}

	@Override
	public String visit(UpdateStatement v)
	{
		return resolver.resolveReturnType(v.getReturnValue());
	}
}
