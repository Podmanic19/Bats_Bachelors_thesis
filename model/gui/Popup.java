package model.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public interface Popup {
    default void popup(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text, ButtonType.CLOSE);
        alert.show();
    }
}