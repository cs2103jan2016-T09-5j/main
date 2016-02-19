import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BasicFX extends Application implements EventHandler<ActionEvent> {

	public static final int DEFAULT_WINDOW_SIZE_WIDTH = 300;
	public static final int DEFAULT_WINDOW_SIZE_HEIGHT = 250;

	Button goButton;

	public static void main(String[] args) {
		launch(args);
	}

	@Override

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Clockwork");

		goButton = new Button();
		goButton.setText("Click Me!");

		goButton.setOnAction(this);

		StackPane layout = new StackPane();
		layout.getChildren().add(goButton);

		Scene scene = new Scene(layout, DEFAULT_WINDOW_SIZE_WIDTH, DEFAULT_WINDOW_SIZE_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == goButton){
			System.out.println("BOO!");
		}
		
	}
}
