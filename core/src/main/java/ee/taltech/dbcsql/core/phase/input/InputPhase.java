package ee.taltech.dbcsql.core.phase.input;

import java.io.InputStream;
import java.util.Locale;

import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.GenerationContextBaseBuilder;
import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.GenerationRequestBuilder;
import ee.taltech.dbcsql.core.phase.input.contract.ContractInputParser;

public class InputPhase
{
	static
	{
		Locale.setDefault(new Locale("en", "US"));
		System.setProperty("file.encoding", "UTF-8");
	}
	private GenerationRequestBuilder builder = new GenerationRequestBuilder();

	public InputPhase withGenerationContext(GenerationContext context)
	{
		this.builder.withContext(context);
		return this;
	}

	public class GenerationContextSubBuilder extends GenerationContextBaseBuilder<GenerationContextSubBuilder>
	{
		private InputPhase owner;
		public GenerationContextSubBuilder(InputPhase owner)
		{
			this.owner = owner;
		}

		public InputPhase build()
		{
			this.owner.withGenerationContext(this.data);
			return this.owner;
		}
	}

	public GenerationContextSubBuilder makeGenerationContext()
	{
		return new GenerationContextSubBuilder(this);
	}

	public InputPhase withContractStream(InputStream stream)
	{
		GenerationContext context = this.builder.getDataContext();
		assert context != null: "Context must be given before contracts";
		this.builder.withContract(new ContractInputParser(context).parse(stream));
		return this;
	}

	public GenerationRequest finishInput()
	{
		return this.builder.build();
	}
}
