package model.main;
import model.agents.AgentParams;
import model.testing.EnvironmentParameters;
import model.map.mapshell.Map;
import model.map.MapParameters;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {
    public static MapParameters mapparams = new MapParameters();
    public static AgentParams agentparams = new AgentParams();
    public static EnvironmentParameters envparams = new EnvironmentParameters();
    public static boolean SHOW_ATTRACTION = false;
    public static boolean SHOW_SIGHT = false;
    public static Stage primaryStage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            mainScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void mainScene() throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/view/mainscene.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Bat simulation");
        primaryStage.getIcons().add(new Image("/Image/images.jfif"));
        primaryStage.show();

    }

}