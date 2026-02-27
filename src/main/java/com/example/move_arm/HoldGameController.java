package com.example.move_arm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.move_arm.model.HoldAttempt;
import com.example.move_arm.model.User;
import com.example.move_arm.model.settings.HoverGameSettings;
import com.example.move_arm.service.AnimationService;
import com.example.move_arm.service.GameService;
import com.example.move_arm.service.SettingsService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class HoldGameController {

    @FXML private Pane gameRoot;
    @FXML private HBox topPanel;

    private Label scoreLabel;
    private Label timeLabel;
    private Label userLabel;

    private int score = 0;
    private int activeCircles = 0;
    private int remainingTime;
    private long gameStartTimeNs;
    private final double HOLD_TIME_SECONDS = 0.5; 
    private int exitAttempts = 0;
    private int globalAttemptCounter = 0;

    private HoverGameSettings settings;
    private final Random random = new Random();
    private boolean gameActive = false;
    private Timeline timer;
    private final List<HoldAttempt> allAttempts = new ArrayList<>();

    private SceneManager sceneManager;
    private final GameService gameService = GameService.getInstance();
    private AudioClip hoverSound;

    // Сохраняем ссылки на слушатели для корректного удаления
    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;
    private boolean isInitialSpawnDone = false;

    @FXML
    public void initialize() {
        // Настройка UI
        scoreLabel = new Label("Очки: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        timeLabel = new Label("Время: 0");
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        userLabel = new Label();
        userLabel.setStyle("-fx-text-fill: #ddd; -fx-font-size: 14px;");

        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setSpacing(18);
        topPanel.getChildren().addAll(scoreLabel, timeLabel, userLabel);

        settings = SettingsService.getInstance().getHoverSettings();

        // Загрузка звука
        try {
            var url = getClass().getResource("/sounds/cartoon-bubble-pop-01-.mp3");
            if (url != null) hoverSound = new AudioClip(url.toExternalForm());
        } catch (Exception ignored) { }

        // Получение SceneManager
        try {
            SceneManager fallback = SceneManager.get();
            if (fallback != null) this.sceneManager = fallback;
        } catch (Exception ignored) { }
        
        updateUserLabel();
    }

    public void setSceneManager(SceneManager manager) {
        this.sceneManager = manager;
        updateUserLabel();
    }

    private void updateUserLabel() {
        try {
            User user = gameService.getCurrentUser();
            userLabel.setText(user != null ? "Пользователь: " + user.getUsername() : "Пользователь: guest");
        } catch (Exception ignored) {}
    }

    public void startGame() {
        if (gameActive) return;

        settings = SettingsService.getInstance().getHoverSettings();
        gameActive = true;
        allAttempts.clear();
        score = 0;
        globalAttemptCounter = 0;
        activeCircles = 0;
        isInitialSpawnDone = false;
        gameStartTimeNs = System.nanoTime();
        exitAttempts = 0;

        remainingTime = settings.getDurationSeconds();
        scoreLabel.setText("Очки: " + score);
        timeLabel.setText("Время: " + remainingTime);
        
        gameRoot.getChildren().clear();

        if (gameRoot.getWidth() <= 0 || gameRoot.getHeight() <= 0) {
            widthListener = (obs, oldVal, newVal) -> {
                if (newVal.doubleValue() > 0 && !isInitialSpawnDone) {
                    spawnInitialTargets();
                }
            };
            
            heightListener = (obs, oldVal, newVal) -> {
                if (newVal.doubleValue() > 0 && !isInitialSpawnDone) {
                    spawnInitialTargets();
                }
            };
            
            gameRoot.widthProperty().addListener(widthListener);
            gameRoot.heightProperty().addListener(heightListener);
        } else {
            spawnInitialTargets();
        }

        startTimer();
    }

    private void spawnInitialTargets() {
        if (widthListener != null) {
            gameRoot.widthProperty().removeListener(widthListener);
            widthListener = null;
        }
        if (heightListener != null) {
            gameRoot.heightProperty().removeListener(heightListener);
            heightListener = null;
        }
        
        isInitialSpawnDone = true;
        
        while (activeCircles < settings.getMaxCirclesCount()) {
            spawnHoldTarget();
        }
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime--;
            timeLabel.setText("Время: " + remainingTime);
            if (remainingTime <= 0) endGame();
        }));
        timer.setCycleCount(settings.getDurationSeconds());
        timer.play();
    }

    private void endGame() {
        gameActive = false;
        if (timer != null) timer.stop();
        gameRoot.getChildren().clear();

        try {
            // Вызываем новый метод вместо старого addGameClicks
            int savedId = gameService.addHoldGameResults(
                    settings.getRadius(),
                    new ArrayList<>(allAttempts) // Твой список HoldAttempt
            );
            AppLogger.info("Результат Hold-игры сохранён (id=" + savedId + ")");
        } catch (Exception e) {
            AppLogger.error("Ошибка сохранения результатов", e);
        }


        if (widthListener != null) {
            gameRoot.widthProperty().removeListener(widthListener);
            widthListener = null;
        }
        if (heightListener != null) {
            gameRoot.heightProperty().removeListener(heightListener);
            heightListener = null;
        }
        SceneManager mgr = (sceneManager != null) ? sceneManager : SceneManager.get();
        if (mgr != null) mgr.showResults(); 
    }

    private void spawnHoldTarget() {
        if (!gameActive) return;

        double paneWidth = gameRoot.getWidth();
        double paneHeight = gameRoot.getHeight();
        if (paneWidth <= 0 || paneHeight <= 0) return;

        int radius = settings.getRadius();
        double x = random.nextDouble() * (paneWidth - radius * 2);
        double y = random.nextDouble() * (paneHeight - radius * 2);
        
        final double centerX = x + radius;
        final double centerY = y + radius;

        Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256), 1.0);

        // Храним время входа для расчета длительности срыва
        final long[] entryTimeNs = {0}; 
        final boolean[] isTargetProcessed = {false};

        // --- ЛОГИКА УСПЕХА (Мишень удержана до конца) ---
        Runnable onComplete = () -> {
            if (!gameActive || isTargetProcessed[0]) return;

            isTargetProcessed[0] = true;

            long now = System.nanoTime();
            
            // 1. ЗАПИСЬ ДАННЫХ: Успешная попытка
            allAttempts.add(new HoldAttempt(
                    ++globalAttemptCounter,
                    entryTimeNs[0],
                    now,
                    (long) (HOLD_TIME_SECONDS * 1000), // фактическое время = требуемому
                    true,
                    centerX,
                    centerY
            ));

            if (hoverSound != null) hoverSound.play();

            score++;
            scoreLabel.setText("Очки: " + score);
            activeCircles--;

            // 2. УДАЛЕНИЕ И АНИМАЦИЯ (Функциональность сохранена)
            gameRoot.getChildren().removeIf(node ->
                    node instanceof HoldTarget &&
                            Math.abs(((HoldTarget) node).getLayoutX() - x) < 0.1 &&
                            Math.abs(((HoldTarget) node).getLayoutY() - y) < 0.1
            );

            // Создаем "пустышку" для анимации взрыва
            Circle explosionDummy = new Circle(radius, color);
            explosionDummy.setCenterX(centerX);
            explosionDummy.setCenterY(centerY);
            gameRoot.getChildren().add(explosionDummy);

            try {
                // Твоя фирменная анимация из AnimationService
                AnimationService.playDestructionAnimation(gameRoot, explosionDummy, null);
            } catch (Exception ignored) {}

            // Удаляем остатки анимации через полсекунды
            new Timeline(new KeyFrame(Duration.millis(500), e -> gameRoot.getChildren().remove(explosionDummy))).play();

            if (activeCircles < settings.getMaxCirclesCount()) {
                spawnHoldTarget();
            }
        };

        // Создаем саму мишень (внутри неё зашита анимация заполнения цветом)
        HoldTarget target = new HoldTarget(radius, color, HOLD_TIME_SECONDS, onComplete);

        // --- ЛОГИКА ВХОДА ---
        target.setOnMouseEntered(e -> {
            entryTimeNs[0] = System.nanoTime();
        });

        // --- ЛОГИКА СРЫВА ---
        target.setOnMouseExited(e -> {
            if (gameActive && entryTimeNs[0] > 0 && !isTargetProcessed[0]) {
                // Если мишень всё еще в детях Pane — значит, это был преждевременный выход
                if (gameRoot.getChildren().contains(target)) {
                    long now = System.nanoTime();
                    long actualMs = (now - entryTimeNs[0]) / 1_000_000;

                    // ЗАПИСЬ ДАННЫХ: Неудачная попытка (срыв)
                    allAttempts.add(new HoldAttempt(
                            ++globalAttemptCounter,
                            entryTimeNs[0],
                            now,
                            actualMs,
                            false,
                            centerX,
                            centerY
                    ));
                    
                    entryTimeNs[0] = 0; // Сбрасываем метку входа
                }
            }
        });

        target.setLayoutX(x);
        target.setLayoutY(y);
        gameRoot.getChildren().add(target);
        activeCircles++;
    }

    @FXML
    private void handleToMenu() {
        if (widthListener != null) {
            gameRoot.widthProperty().removeListener(widthListener);
            widthListener = null;
        }
        if (heightListener != null) {
            gameRoot.heightProperty().removeListener(heightListener);
            heightListener = null;
        }
        
        if (timer != null) timer.stop();
        gameActive = false;
        SceneManager mgr = (sceneManager != null) ? sceneManager : SceneManager.get();
        if (mgr != null) mgr.showMenu();
    }
    
    @FXML 
    private void handleRestart() {
        handleToMenu();
        startGame();
    }
    
    // ✅ Метод сохранения результатов (закомментирован как просили)
    /*
    private void saveResults() {
        try {
            User user = gameService.getCurrentUser();
            if (user != null) {
                gameService.saveGameResult(user.getId(), "hold_game", score, settings.getDurationSeconds());
                AppLogger.info("Результаты сохранены для пользователя: " + user.getUsername());
            }
        } catch (Exception e) {
            AppLogger.error("Ошибка сохранения результатов", e);
        }
    }
    */
}