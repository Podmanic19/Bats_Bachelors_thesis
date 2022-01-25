package Main;
import Classes.AgentParams;
import Environment.EnvironmentMap;
import Environment.EnvironmentParameters;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static EnvironmentParameters envparams = new EnvironmentParameters();
    public static AgentParams agentparams = new AgentParams();
    public static EnvironmentMap envMap;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/Scenes/mainscene.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Bat simulation");
        primaryStage.getIcons().add(new Image("/Image/images.jfif"));
        primaryStage.show();
    }
}