package ee.taltech.dbcsql.iface.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;
import ee.taltech.dbcsql.core.phase.input.InputPhase;
import ee.taltech.dbcsql.core.phase.output.OutputPhase;
import ee.taltech.dbcsql.core.phase.output.persistence.MemoryPersistence;
import ee.taltech.dbcsql.db.postgres.PostgresPlatform;

public class Main
{
	@Parameter(names = { "-cx", "--context" }, description = "Context file to use", required = true)
	private String contextFile;

	@Parameter(names = { "-ct", "--contract" }, description = "Contract file to use", required = true)
	private String contractFile;

	@Parameter(names = { "-si", "--security-invoker" }, description = "Make the operation into a SECURITY INVOKER (DEFINER by default)")
	private boolean invoker = false;

	@Parameter(names = { "-ir", "--implicit-return" }, description = "Have the last operation return identifier")
	private boolean implicitReturn = true;

	@Parameter(names = { "-ap", "--argument-prefix" }, description = "Argument prefix")
	private String argumentPrefix = "";

	@Parameter(names = { "-op", "--operation-prefix" }, description = "Operation prefix")
	private String operationPrefix = "";

	public static void main(String... argv)
	{
		Main main = new Main();
		JCommander jcom = JCommander
			.newBuilder()
			.addObject(main)
			.build()
		;
		jcom.setProgramName("dbcsql-cli");
		jcom.parse(argv);
		main.run();
	}

	public void run()
	{
		try
		{
			MemoryPersistence persistence = new MemoryPersistence();

			GenerationRequest request = new InputPhase()
				.makeGenerationContext()
					.withPlatform(new PostgresPlatform())
					.withPersistence(persistence)
					.withContextFromStream(streamFile(contextFile))
					.withSecurityInvoker(invoker)
					.withReturnOnLastPostcondition(implicitReturn)
					.withFunctionPrefix(operationPrefix)
					.withArgumentPrefix(argumentPrefix)
				.build()
				.withContractStream(streamFile(contractFile))
			.finishInput()
			;

			new OutputPhase()
				.processRequest(request)
			;

			System.out.println(persistence.getMemory().toString());
		}
		catch (TranslatorInputException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public InputStream streamFile(String fname)
	{
		try
		{
			return new FileInputStream(fname);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
