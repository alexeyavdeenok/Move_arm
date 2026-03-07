package com.example.move_arm.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.example.move_arm.comtroller.*;
import com.example.move_arm.util.AppLogger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;
    private final Stage primaryStage;
    private final Map<String, Scene> sceneCache = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    // Ключи для сцен
    public static final String START = "start";
    public static final String GAME = "game";
    public static final String RESULTS = "results";
    public static final String MORERESULTS = "moreresults";
    public static final String SETTINGS = "settings";
    public static final String SELECTION = "selection";
    public static final String MENU = "menu";
    public static final String STATISTICS = "statistics";


    private SceneManager(Stage stage) {
        this.primaryStage = stage;
    }

    public static void init(Stage stage) {
        if (instance == null) {
            instance = new SceneManager(stage);
            AppLogger.info("SceneManager: Инициализирован");
        }
    }

    public static SceneManager get() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager не инициализирован. Вызовите init() в start().");
        }
        return instance;
    }

    // Универсальная загрузка сцены
    private <T> void loadScene(
            String key,
            String fxmlPath,
            Class<T> controllerType,
            Consumer<T> afterLoad
    ) {
        try {

            if (sceneCache.containsKey(key)) {
                primaryStage.setScene(sceneCache.get(key));
                T controller = controllerType.cast(controllers.get(key));

                if (afterLoad != null) {
                    Platform.runLater(() -> afterLoad.accept(controller));
                }

                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T controller = loader.getController();

            Rectangle2D screen = Screen.getPrimary().getBounds();
            Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());

            sceneCache.put(key, scene);
            controllers.put(key, controller);

            primaryStage.setScene(scene);

            if (afterLoad != null) {
                Platform.runLater(() -> afterLoad.accept(controller));
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки сцены: " + key, e);
        }
    }

    // === Публичные методы ===

    public void clearCache() {
        sceneCache.clear();
        controllers.clear();
        AppLogger.info("SceneManager: Кэш сцен очищен");
    }

    public void removeFromCache(String sceneKey) {
        sceneCache.remove(sceneKey);
        controllers.remove(sceneKey);
        AppLogger.info("SceneManager: Сцена '" + sceneKey + "' удалена из кэша");
    }

    public void showStart() {
        loadScene(
                START,
                "/com/example/move_arm/fxml/start-window.fxml",
                StartWindowController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showResults(){
        removeFromCache(RESULTS);
        loadScene(
                RESULTS,
                "/com/example/move_arm/fxml/results-view.fxml",
                ResultsController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showMoreResults(){
        removeFromCache(MORERESULTS);
        loadScene(MORERESULTS,
                "/com/example/move_arm/fxml/more-results-view.fxml",
                MoreResultsController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showSelection() {
        loadScene(SELECTION,
                "/com/example/move_arm/fxml/selection-view.fxml",
                SelectionController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showMenu() {
        loadScene(MENU,
                "/com/example/move_arm/fxml/menu-view.fxml",
                MenuController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showStatistics() {
        loadScene(STATISTICS,
                "/com/example/move_arm/fxml/hover-statistics-view.fxml",
                StatisticsController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void showSettings() {
        loadScene(SETTINGS,
                "/com/example/move_arm/fxml/settings-view.fxml",
                SettingsController.class,
                ctrl -> ctrl.setSceneManager(this)
        );
    }

    public void startNewGame() {
        loadScene(
                GAME,
                "/com/example/move_arm/fxml/game-view.fxml",
                GameController.class,
                ctrl -> {
                    ctrl.setSceneManager(this);
                    ctrl.startGame();
                }
        );

    }

    public void showHoldGame() {

        loadScene(
                "holdGame",
                "/com/example/move_arm/fxml/hold-game-view.fxml",
                HoldGameController.class,
                ctrl -> {
                    ctrl.setSceneManager(this);
                    ctrl.startGame();
                }
        );

    }
}