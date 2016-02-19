import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClockworkGUI extends Application {

	public static final int DEFAULT_WINDOW_WIDTH = 600;
	public static final int DEFAULT_WINDOW_HEIGHT = 400;
	
	public static final Text welcomeText = new Text("Welcome, Username");

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage window) {
		window.setTitle("Clockwork");
		BorderPane defaultLayout = new BorderPane();
		defaultLayout.setPadding(new Insets(10));

		/** Formatting Top Region */
		HBox welcomeSection = addWelcomeSection(welcomeText);
		defaultLayout.setTop(welcomeSection);

		/** Formatting Left Region */
		Button btnLeft = new Button("Left");
		defaultLayout.setLeft(btnLeft);

		/** Formatting Center Region */
		Button btnCenter = new Button("Center");
		defaultLayout.setCenter(btnCenter);

		/** Formatting Right Region */
		Button btnRight = new Button("Right");
		defaultLayout.setRight(btnRight);

		/** Formatting Bottom Region */
		Button btnBottom = new Button("Bottom");
		defaultLayout.setBottom(btnBottom);

		/** Setting Default Scene */
		Scene scene = new Scene(defaultLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		window.setScene(scene);
		window.show();
	}
	
	/** Add Welcome Message in Horizontal Row*/
	public HBox addWelcomeSection(Text welcomeText) {
	    HBox welcomeBox = new HBox();
	    welcomeBox.setPadding(new Insets(15, 12, 15, 12));
	    welcomeBox.setSpacing(10);
	    welcomeBox.setStyle("-fx-background-color: #336699;");

	    welcomeText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    welcomeText.setFill(Color.WHITE);
	    welcomeText.setStroke(Color.web("#7080A0"));
	    welcomeBox.getChildren().add(welcomeText);

	    return welcomeBox;
	}
}