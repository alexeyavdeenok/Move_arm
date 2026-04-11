package com.example.move_arm.ui.view;

import com.example.move_arm.HoldTarget;
import com.example.move_arm.service.AnimationService;
import com.example.move_arm.util.AppLogger;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HoldGameView {

    @FXML private Pane gameRoot;
    @FXML private HBox topPanel;

    private Label scoreLabel;
    private Label timeLabel;
    private Label userLabel;

    private Runnable onToMenuHandler;
    private Runnable onRestartHandler;
    private Runnable onViewReadyHandler;
    private boolean viewReadyNotified = false;

    @FXML
    public void initialize() {
        scoreLabel = new Label("Очки: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        timeLabel = new Label("Время: 0");
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        userLabel = new Label();
        userLabel.setStyle("-fx-text-fill: #ddd; -fx-font-size: 14px;");

        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setSpacing(18);
        topPanel.getChildren().addAll(scoreLabel, timeLabel, userLabel);

        setupViewReadyListener();
    }

    private void setupViewReadyListener() {
        gameRoot.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                gameRoot.widthProperty().addListener((wObs, oldW, newW) -> checkIfViewReady());
                gameRoot.heightProperty().addListener((hObs, oldH, newH) -> checkIfViewReady());
                checkIfViewReady();
            }
        });
    }

    private void checkIfViewReady() {
        if (!viewReadyNotified && gameRoot.getWidth() > 100 && gameRoot.getHeight() > 100 && onViewReadyHandler != null) {
            viewReadyNotified = true;
            AppLogger.info("HoldGameView: view ready");
            onViewReadyHandler.run();
        }
    }

    public void start() {
        viewReadyNotified = false;
        clearField();
        setScore(0);
        setTime(0);
        checkIfViewReady();
    }

    public void clearField() {
        gameRoot.getChildren().clear();
    }

    public HoldTarget addHoldTarget(
            double x,
            double y,
            int radius,
            Color color,
            double holdDurationSeconds,
            Runnable onComplete,
            Runnable onEntered,
            Runnable onExited
    ) {
        HoldTarget target = new HoldTarget(radius, color, holdDurationSeconds, onComplete);
        target.setLayoutX(x);
        target.setLayoutY(y);
        target.setOnMouseEntered(event -> {
            if (onEntered != null) {
                onEntered.run();
            }
        });
        target.setOnMouseExited(event -> {
            if (onExited != null) {
                onExited.run();
            }
        });
        gameRoot.getChildren().add(target);
        return target;
    }

    public boolean containsTarget(HoldTarget target) {
        return gameRoot.getChildren().contains(target);
    }

    public void removeTarget(HoldTarget target) {
        gameRoot.getChildren().remove(target);
    }

    public void playDestroyAnimation(double centerX, double centerY, int radius, Color color) {
        Circle explosionDummy = new Circle(radius, color);
        explosionDummy.setCenterX(centerX);
        explosionDummy.setCenterY(centerY);
        gameRoot.getChildren().add(explosionDummy);

        try {
            AnimationService.playDestructionAnimation(gameRoot, explosionDummy, null);
        } catch (Exception ignored) {
        }
    }

    public void setScore(int score) {
        scoreLabel.setText("Очки: " + score);
    }

    public void setTime(int seconds) {
        timeLabel.setText("Время: " + seconds);
    }

    public void setUserName(String username) {
        String actualName = (username == null || username.isBlank()) ? "guest" : username;
        userLabel.setText("Пользователь: " + actualName);
    }

    public void setOnToMenu(Runnable handler) {
        this.onToMenuHandler = handler;
    }

    public void setOnRestart(Runnable handler) {
        this.onRestartHandler = handler;
    }

    public void setOnViewReady(Runnable handler) {
        this.onViewReadyHandler = handler;
        checkIfViewReady();
    }

    public double getWidth() {
        return gameRoot.getWidth();
    }

    public double getHeight() {
        return gameRoot.getHeight();
    }

    @FXML
    private void handleToMenu() {
        if (onToMenuHandler != null) {
            onToMenuHandler.run();
        }
    }

    @FXML
    private void handleRestart() {
        if (onRestartHandler != null) {
            onRestartHandler.run();
        }
    }
}
