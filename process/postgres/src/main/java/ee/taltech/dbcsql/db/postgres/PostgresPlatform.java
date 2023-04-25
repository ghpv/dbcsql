package ee.taltech.dbcsql.db.postgres;

import ee.taltech.dbcsql.core.db.TypeInterpreter;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.output.OutputGenerator;
import ee.taltech.dbcsql.process.postgres.PostgresOutputGenerator;

public class PostgresPlatform implements TargetPlatform
{
	@Override
	public String getProductName()
	{
		return "PostgreSQL";
	}

	@Override
	public OutputGenerator getOutputGenerator(GenerationContext context)
	{
		return new PostgresOutputGenerator(context);
	}

	@Override
	public TypeInterpreter getTypeInterpreter()
	{
		return new PostgresTypeInterpreter();
	}
}
