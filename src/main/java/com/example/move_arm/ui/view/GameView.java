package com.example.move_arm.ui.view;

import java.util.List;
import java.util.function.Consumer;

import javafx.scene.paint.Color;

public interface GameView {

    void start();                    // подготовка UI перед запуском игры
    void clearField();
    void addTarget(double x, double y, int radius, Color color);
    void removeTarget(Object targetId);   // можно передавать Circle или любой идентификатор

    void setScore(int score);
    void setTime(int seconds);
    void setUserName(String username);

    // События
    void setOnTargetHit(Consumer<TargetHitEvent> handler);
    void setOnToMenu(Runnable handler);
    void setOnRestart(Runnable handler);

    // Важно: View сообщает, когда он готов (размеры посчитаны)
    void setOnViewReady(Runnable onReady);

    double getWidth();
    double getHeight();
    List<double[]> getActiveTargetPositions();
}