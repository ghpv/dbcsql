package ee.taltech.dbcsql.process.postgres;

import org.stringtemplate.v4.ST;

import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.sql.Function;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.output.OutputGenerator;
import ee.taltech.dbcsql.process.postgres.translate.ModelTranslator;

public class PostgresOutputGenerator extends OutputGenerator
{
	private ModelTranslator translator;

	public PostgresOutputGenerator(GenerationContext context)
	{
		super(context);
		this.translator = new ModelTranslator(context);
	}

	protected void generateContractImpl(ContractDef contract)
	{
		Function func = translator.translateContract(contract);
		ST st = PostgresTemplates
			.instance()
			.getFunction()
			.add("d", func)
		;
		this.getPersistence().write(st.render());
	}
}
