package logic;

import javafx.scene.control.Alert;

public interface ShowAlert {
    default void show(String title, String message, int height){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setHeight(height);
        alert.showAndWait();
    }
}
