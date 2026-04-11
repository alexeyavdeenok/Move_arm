package com.example.move_arm.app;

import com.example.move_arm.ui.SceneManager;
import com.example.move_arm.util.AppLogger;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelloApplication extends Application {

    @Override
    public void init() {
        AppLogger.info("HelloApplication: init() - приложение инициализируется");
    }

    @Override
    public void start(Stage stage) {
        AppLogger.info("HelloApplication: start() - запуск JavaFX приложения");

        try {
            // 1. Инициализируем SceneManager
            SceneManager.init(stage);

            // 2. Загружаем главный контейнер с регионом (но пока не показываем окно)
            SceneManager.get().showMainLayout();

            // 3. Показываем начальный экран (логин)
            SceneManager.get().showStart();

            // === Настройки окна ===
            stage.setTitle("Move Arm - Управление рукой");
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            // Растягиваем на весь экран
            Rectangle2D screen = Screen.getPrimary().getBounds();
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(screen.getWidth());
            stage.setHeight(screen.getHeight());

            // Логирование событий
            stage.setOnShowing(e -> AppLogger.info("HelloApplication: Окно показывается"));
            stage.setOnShown(e -> AppLogger.info("HelloApplication: Окно показано"));
            stage.setOnCloseRequest(e -> AppLogger.info("HelloApplication: Запрос на закрытие окна"));

            // 4. Показываем окно
            stage.show();

            AppLogger.info("HelloApplication: Приложение запущено успешно (начальный экран: Start)");

        } catch (Exception e) {
            AppLogger.error("HelloApplication: Критическая ошибка при запуске", e);
            throw e;
        }
    }

    @Override
    public void stop() {
        AppLogger.info("HelloApplication: stop() - приложение завершает работу");
    }

    public static void main(String[] args) {
        launch(args);
    }
}