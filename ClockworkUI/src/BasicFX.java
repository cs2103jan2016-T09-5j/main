import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BasicFX extends Application {

	public static final int DEFAULT_WINDOW_SIZE = 500;
	
	Label labelText;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		labelText = new Label("Here are some text");

		VBox root = new VBox();
		root.getChildren().add(labelText);

		Scene scene = new Scene(root, DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_SIZE);
		stage.setScene(scene);

		stage.show();
	}
}
