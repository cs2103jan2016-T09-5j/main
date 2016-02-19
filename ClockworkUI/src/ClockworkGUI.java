import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		BorderPane defaultLayout = new BorderPane();
		defaultLayout.setPadding(new Insets(10));

		Button btnTop = new Button("Top");
		defaultLayout.setTop(btnTop);

		Button btnLeft = new Button("Left");
		defaultLayout.setLeft(btnLeft);

		Button btnCenter = new Button("Center");
		defaultLayout.setCenter(btnCenter);

		Button btnRight = new Button("Right");
		defaultLayout.setRight(btnRight);

		Button btnBottom = new Button("Bottom");
		defaultLayout.setBottom(btnBottom);

		Scene scene = new Scene(defaultLayout, 300, 200);
		window.setScene(scene);
		window.show();
	}
}