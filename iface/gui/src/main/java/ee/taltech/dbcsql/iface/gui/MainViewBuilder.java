package ee.taltech.dbcsql.iface.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;
import ee.taltech.dbcsql.core.phase.input.InputPhase;
import ee.taltech.dbcsql.core.phase.output.OutputPhase;
import ee.taltech.dbcsql.core.phase.output.persistence.MemoryPersistence;
import ee.taltech.dbcsql.db.postgres.PostgresPlatform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class MainViewBuilder
{
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 800;
	private GridPane grid = new GridPane();
	private Scene mainScene = new Scene(grid, WIDTH, HEIGHT);
	private TextArea contextView;
	private TextArea contractView;
	private TextArea outputView;
	private CheckBox invoker = new CheckBox("Security Invoker");
	private CheckBox implicitReturn = new CheckBox("Implicit return last");
	private TextField paramPrefix = new TextField();
	private TextField opPrefix = new TextField();
	private MemoryPersistence memoryPersistence = new MemoryPersistence();

	public MainViewBuilder()
	{
		// TOP VIEW
		this.contextView = makeContextView();
		this.contractView = makeContractView();
		this.outputView = makeOutputView();
		this.paramPrefix.setPromptText("Parameter name prefix");
		this.opPrefix.setPromptText("Function name prefix");
		grid.addRow(
			0,
			this.contextView,
			this.contractView,
			this.outputView
		);

		GridPane.setHgrow(this.contextView, Priority.ALWAYS);
		GridPane.setHgrow(this.contractView, Priority.ALWAYS);
		GridPane.setHgrow(this.outputView, Priority.ALWAYS);
		GridPane.setVgrow(this.contextView, Priority.ALWAYS);
		GridPane.setVgrow(this.contractView, Priority.ALWAYS);
		GridPane.setVgrow(this.outputView, Priority.ALWAYS);

		// OPTIONS
		invoker.setAllowIndeterminate(false);
		invoker.setSelected(false);
		implicitReturn.setAllowIndeterminate(false);
		implicitReturn.setSelected(true);
		VBox options = new VBox();
		options
			.getChildren()
			.addAll(
				opPrefix,
				paramPrefix,
				invoker,
				implicitReturn
			);

		for (Node n: options.getChildren())
		{
			GridPane.setHalignment(n, HPos.LEFT);
			GridPane.setValignment(n, VPos.TOP);
		}
		grid.add(options, 1, 1);

		// GENERATE
		Button gen = makeGenerateButton();
		grid.add(gen, 2, 1);
		GridPane.setHalignment(gen, HPos.LEFT);
		GridPane.setValignment(gen, VPos.TOP);

		// GENERIC
		grid.getRowConstraints().setAll
		(
			new RowConstraints(),
			new RowConstraints(100)
		);
	}

	private TextArea makeContextView()
	{
		TextArea contextView = new TextArea();
		contextView.setText("table test\n{\n\tid INTEGER;\n};\nidentifier for test is id;");
		contextView.setPromptText("// Context goes here");
		return contextView;
	}

	private TextArea makeContractView()
	{
		TextArea contractView = new TextArea();
		contractView.setText("operation add_test\n{\n\tp_id;\n}\npreconditions\n{\n}\npostconditions\n{\n\tinserted test a\n\t{\n\t\tid = p_id;\n\t};\n}");
		contractView.setPromptText("// Contract goes here");
		return contractView;
	}

	private TextArea makeOutputView()
	{
		TextArea outputView = new TextArea();
		outputView.setEditable(false);
		outputView.setPromptText("Output will appear here");
		return outputView;
	}

	private Button makeGenerateButton()
	{
		Button btn = new Button();
		btn.setText("Generate");
		btn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				String output = "";
				try
				{
					GenerationRequest request = new InputPhase()
						.makeGenerationContext()
							.withPlatform(new PostgresPlatform())
							.withPersistence(memoryPersistence)
							.withContextFromStream(streamFromString(contextView.getText()))
							.withSecurityInvoker(invoker.isSelected())
							.withReturnOnLastPostcondition(implicitReturn.isSelected())
							.withParameterPrefix(paramPrefix.getText())
							.withFunctionPrefix(opPrefix.getText())
						.build()
						.withContractStream(streamFromString(contractView.getText()))
					.finishInput();
					;

					new OutputPhase()
						.processRequest(request)
					;

					output = memoryPersistence.getMemory().toString();
				}
				catch (TranslatorInputException e)
				{
					output = e.getMessage();
				}

				outputView.setText(output);
			}
		});
		return btn;
	}

	private InputStream streamFromString(String str)
	{
		return new ByteArrayInputStream(str.getBytes());
	}

	public Scene build()
	{
		return mainScene;
	}
}
