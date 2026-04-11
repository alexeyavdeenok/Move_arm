// com.example.move_arm.ui.MainLayoutController.java
package com.example.move_arm.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML
    private StackPane contentArea;

    /** Главный метод — заменяет текущий экран */
    public void switchContent(Parent newContent) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(newContent);
    }
}