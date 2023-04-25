package ee.taltech.dbcsql.core.phase.output;

import ee.taltech.dbcsql.core.phase.GenerationRequest;

public class OutputPhase
{
	public OutputPhase processRequest(GenerationRequest request)
	{
		OutputGenerator outputGenerator = request
			.getContext()
			.getPlatform()
			.getOutputGenerator(request.getContext())
		;
		outputGenerator.generate(request);

		return this;
	}

}
