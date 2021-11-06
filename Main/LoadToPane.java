package Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.Objects;

public interface LoadToPane {

    default void load(Pane pane, String view) throws IOException {
        pane.getChildren().clear();
        Pane pane2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Scenes/" + view + ".fxml")));
        pane.getChildren().add(pane2);
    }

}
