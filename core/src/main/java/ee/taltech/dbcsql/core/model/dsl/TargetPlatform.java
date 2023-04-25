package ee.taltech.dbcsql.core.model.dsl;

import ee.taltech.dbcsql.core.db.TypeInterpreter;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.output.OutputGenerator;

public interface TargetPlatform
{
	String getProductName();
	OutputGenerator getOutputGenerator(GenerationContext context);
	TypeInterpreter getTypeInterpreter();
}
