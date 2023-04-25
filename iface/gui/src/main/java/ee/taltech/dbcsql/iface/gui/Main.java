package ee.taltech.dbcsql.iface.gui;
 
import javafx.application.Application;
import javafx.stage.Stage;
 
public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		MainViewBuilder mvBuilder = new MainViewBuilder();

		primaryStage.setTitle("Design by contract SQL generator");
		primaryStage.setScene(mvBuilder.build());
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
