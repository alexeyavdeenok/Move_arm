package com.example.move_arm.service;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DestroyAnimationService {

    // =========================
    // ✅ ПРОСТОЕ ИСЧЕЗНОВЕНИЕ
    // =========================
    public static void playSimple(Pane root, Circle circle, Runnable onFinish) {

        // Полностью блокируем события
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        FadeTransition fade = new FadeTransition(Duration.millis(180), circle);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(180), circle);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.1);
        scale.setToY(0.1);

        ParallelTransition anim = new ParallelTransition(fade, scale);

        anim.setOnFinished(e -> {
            root.getChildren().remove(circle);
            if (onFinish != null) onFinish.run();
        });

        anim.play();
    }

    // =========================
    // ✅ СТАБИЛЬНЫЙ "ВЗРЫВ"
    // =========================
    public static void playExplosion(Pane root, Circle circle, Runnable onFinish) {

        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        for (int i = 0; i < 12; i++) {
            Circle particle = new Circle(3, color);
            particle.setCenterX(cx);
            particle.setCenterY(cy);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            double angle = Math.random() * Math.PI * 2;
            double distance = 60 + Math.random() * 40;

            TranslateTransition move = new TranslateTransition(Duration.millis(300), particle);
            move.setByX(Math.cos(angle) * distance);
            move.setByY(Math.sin(angle) * distance);

            FadeTransition fade = new FadeTransition(Duration.millis(300), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ParallelTransition anim = new ParallelTransition(move, fade);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            anim.play();
        }

        // ✅ Callback вызывается сразу — без тайминговых ловушек
        if (onFinish != null) onFinish.run();
    }

    public static void playCrazyExplosion(Pane root, Circle circle, Runnable onFinish) {

        // Полностью отключаем события
        circle.setOnMouseEntered(null);
        circle.setOnMouseClicked(null);
        circle.setMouseTransparent(true);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double baseRadius = circle.getRadius();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        // ==========================
        // ⚡ ВСПЫШКА
        // ==========================
        Circle flash = new Circle(baseRadius, color);
        flash.setCenterX(cx);
        flash.setCenterY(cy);
        flash.setMouseTransparent(true);
        root.getChildren().add(flash);

        ScaleTransition flashScale = new ScaleTransition(Duration.millis(120), flash);
        flashScale.setFromX(1);
        flashScale.setFromY(1);
        flashScale.setToX(2.2);
        flashScale.setToY(2.2);

        FadeTransition flashFade = new FadeTransition(Duration.millis(120), flash);
        flashFade.setFromValue(1);
        flashFade.setToValue(0);

        ParallelTransition flashAnim = new ParallelTransition(flashScale, flashFade);
        flashAnim.setOnFinished(e -> root.getChildren().remove(flash));
        flashAnim.play();

        // ==========================
        // 💥 ОСКОЛКИ (16 ШТУК)
        // ==========================
        for (int i = 0; i < 16; i++) {
            Circle particle = new Circle(3 + Math.random() * 3, color);
            particle.setCenterX(cx);
            particle.setCenterY(cy);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            double angle = Math.random() * Math.PI * 2;
            double distance = 80 + Math.random() * 60;

            TranslateTransition move = new TranslateTransition(Duration.millis(450), particle);
            move.setByX(Math.cos(angle) * distance);
            move.setByY(Math.sin(angle) * distance);

            FadeTransition fade = new FadeTransition(Duration.millis(450), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ScaleTransition scale = new ScaleTransition(Duration.millis(450), particle);
            scale.setFromX(1);
            scale.setFromY(1);
            scale.setToX(0.2);
            scale.setToY(0.2);

            ParallelTransition anim = new ParallelTransition(move, fade, scale);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            anim.play();
        }

        if (onFinish != null) onFinish.run();
    }

    // =========================
    // 💥 СУМАСШЕДШИЙ ВЗРЫВ 
    // =========================
    public static void playInsaneExplosion(Pane root, Circle circle, Runnable onFinish) {
        // Полная блокировка событий
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);
        circle.setOnMouseClicked(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double baseRadius = circle.getRadius();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        // ==========================
        // 🌟 МЕГА-ВСПЫШКА
        // ==========================
        Circle megaFlash = new Circle(baseRadius * 1.5, color);
        megaFlash.setCenterX(cx);
        megaFlash.setCenterY(cy);
        megaFlash.setMouseTransparent(true);
        root.getChildren().add(megaFlash);

        ScaleTransition flashScale = new ScaleTransition(Duration.millis(100), megaFlash);
        flashScale.setFromX(1);
        flashScale.setFromY(1);
        flashScale.setToX(4.0);
        flashScale.setToY(4.0);

        FadeTransition flashFade = new FadeTransition(Duration.millis(100), megaFlash);
        flashFade.setFromValue(1);
        flashFade.setToValue(0);

        ParallelTransition flashAnim = new ParallelTransition(flashScale, flashFade);
        flashAnim.setOnFinished(e -> root.getChildren().remove(megaFlash));
        flashAnim.play();

        // ==========================
        // 💫 УДАРНАЯ ВОЛНА
        // ==========================
        Circle shockwave = new Circle(baseRadius * 0.5);
        shockwave.setFill(Color.TRANSPARENT);
        shockwave.setStroke(Color.WHITE);
        shockwave.setStrokeWidth(3);
        shockwave.setCenterX(cx);
        shockwave.setCenterY(cy);
        shockwave.setMouseTransparent(true);
        root.getChildren().add(shockwave);

        ScaleTransition waveScale = new ScaleTransition(Duration.millis(400), shockwave);
        waveScale.setFromX(1);
        waveScale.setFromY(1);
        waveScale.setToX(8.0);
        waveScale.setToY(8.0);

        FadeTransition waveFade = new FadeTransition(Duration.millis(400), shockwave);
        waveFade.setFromValue(0.8);
        waveFade.setToValue(0);

        ParallelTransition waveAnim = new ParallelTransition(waveScale, waveFade);
        waveAnim.setOnFinished(e -> root.getChildren().remove(shockwave));
        waveAnim.play();

        // ==========================
        // 🔥 ОСНОВНЫЕ ОСКОЛКИ (32 ШТУКИ)
        // ==========================
        for (int i = 0; i < 32; i++) {
            Circle particle = new Circle(2 + Math.random() * 4, color);
            particle.setCenterX(cx);
            particle.setCenterY(cy);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            double angle = Math.random() * Math.PI * 2;
            double distance = 120 + Math.random() * 100;
            double speed = 300 + Math.random() * 200;

            TranslateTransition move = new TranslateTransition(Duration.millis(speed), particle);
            move.setByX(Math.cos(angle) * distance);
            move.setByY(Math.sin(angle) * distance);

            FadeTransition fade = new FadeTransition(Duration.millis(speed), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ScaleTransition scale = new ScaleTransition(Duration.millis(speed), particle);
            scale.setFromX(1);
            scale.setFromY(1);
            scale.setToX(0.1);
            scale.setToY(0.1);

            // Случайное вращение
            RotateTransition rotate = new RotateTransition(Duration.millis(speed), particle);
            rotate.setFromAngle(0);
            rotate.setToAngle(360 + Math.random() * 720);

            ParallelTransition anim = new ParallelTransition(move, fade, scale, rotate);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            
            // Случайная задержка для более хаотичного эффекта
            anim.setDelay(Duration.millis(Math.random() * 50));
            anim.play();
        }

        // ==========================
        // ✨ МЕЛКИЕ ЧАСТИЦЫ (64 ШТУКИ)
        // ==========================
        for (int i = 0; i < 64; i++) {
            Circle spark = new Circle(1 + Math.random() * 2, 
                Math.random() > 0.5 ? color : Color.WHITE);
            spark.setCenterX(cx);
            spark.setCenterY(cy);
            spark.setMouseTransparent(true);

            root.getChildren().add(spark);

            double angle = Math.random() * Math.PI * 2;
            double distance = 60 + Math.random() * 80;
            double speed = 200 + Math.random() * 150;

            TranslateTransition move = new TranslateTransition(Duration.millis(speed), spark);
            move.setByX(Math.cos(angle) * distance);
            move.setByY(Math.sin(angle) * distance);

            FadeTransition fade = new FadeTransition(Duration.millis(speed), spark);
            fade.setFromValue(1);
            fade.setToValue(0);

            ParallelTransition anim = new ParallelTransition(move, fade);
            anim.setOnFinished(e -> root.getChildren().remove(spark));
            
            // Большая задержка для эффекта "послевзрывных" искр
            anim.setDelay(Duration.millis(50 + Math.random() * 100));
            anim.play();
        }

        // ==========================
        // 💥 ДОПОЛНИТЕЛЬНЫЕ ЭФФЕКТЫ
        // ==========================
        
        // Эффект "ряби"
        for (int i = 0; i < 3; i++) {
            Circle ripple = new Circle(baseRadius);
            ripple.setFill(Color.TRANSPARENT);
            ripple.setStroke(Color.rgb(255, 255, 255, 0.3));
            ripple.setStrokeWidth(2);
            ripple.setCenterX(cx);
            ripple.setCenterY(cy);
            ripple.setMouseTransparent(true);
            root.getChildren().add(ripple);

            ScaleTransition rippleScale = new ScaleTransition(Duration.millis(600), ripple);
            rippleScale.setFromX(1);
            rippleScale.setFromY(1);
            rippleScale.setToX(3.0);
            rippleScale.setToY(3.0);

            FadeTransition rippleFade = new FadeTransition(Duration.millis(600), ripple);
            rippleFade.setFromValue(0.6);
            rippleFade.setToValue(0);

            ParallelTransition rippleAnim = new ParallelTransition(rippleScale, rippleFade);
            rippleAnim.setOnFinished(e -> root.getChildren().remove(ripple));
            rippleAnim.setDelay(Duration.millis(i * 100));
            rippleAnim.play();
        }

        // Крупные обломки
        for (int i = 0; i < 8; i++) {
            Circle debris = new Circle(4 + Math.random() * 6, color);
            debris.setCenterX(cx);
            debris.setCenterY(cy);
            debris.setMouseTransparent(true);

            root.getChildren().add(debris);

            double angle = Math.random() * Math.PI * 2;
            double distance = 40 + Math.random() * 60;
            double rotationSpeed = 180 + Math.random() * 360;

            TranslateTransition move = new TranslateTransition(Duration.millis(800), debris);
            move.setByX(Math.cos(angle) * distance);
            move.setByY(Math.sin(angle) * distance);

            RotateTransition rotate = new RotateTransition(Duration.millis(800), debris);
            rotate.setByAngle(Math.random() > 0.5 ? rotationSpeed : -rotationSpeed);

            FadeTransition fade = new FadeTransition(Duration.millis(800), debris);
            fade.setFromValue(1);
            fade.setToValue(0);

            ScaleTransition scale = new ScaleTransition(Duration.millis(800), debris);
            scale.setToX(0.5);
            scale.setToY(0.5);

            ParallelTransition anim = new ParallelTransition(move, rotate, fade, scale);
            anim.setOnFinished(e -> root.getChildren().remove(debris));
            anim.play();
        }

        if (onFinish != null) onFinish.run();
    }

    public static void playGravityFall(Pane root, Circle circle, Runnable onFinish) {
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        // Создаем 12 частиц
        for (int i = 0; i < 12; i++) {
            Circle particle = new Circle(2 + Math.random() * 3, color);
            particle.setCenterX(cx);
            particle.setCenterY(cy);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            // Простая гравитация через TranslateTransition
            TranslateTransition fall = new TranslateTransition(Duration.millis(800 + Math.random() * 400), particle);
            fall.setByY(80 + Math.random() * 60); // Падение вниз
            fall.setByX((Math.random() - 0.5) * 40); // Небольшое смещение в стороны

            FadeTransition fade = new FadeTransition(Duration.millis(800), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            // Эффект "ускорения" через интерполятор
            fall.setInterpolator(Interpolator.EASE_IN);

            ParallelTransition anim = new ParallelTransition(fall, fade);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            anim.play();
        }

        if (onFinish != null) onFinish.run();
    }
    // =========================
    // 🎯 РАССЫПАНИЕ С ЗАПОЛНЕНИЕМ ПЛОЩАДИ КРУГА
    // =========================
    public static void playAreaGravityFall(Pane root, Circle circle, Runnable onFinish) {
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double radius = circle.getRadius();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        // ==========================
        // 🔢 РАСЧЕТ КОЛИЧЕСТВА ЧАСТИЦ ПО ПЛОЩАДИ
        // ==========================
        double area = Math.PI * radius * radius;
        int particleCount = Math.max(12, (int)(area / 15)); // Больше частиц для больших кругов

        // ==========================
        // 🎲 СОЗДАНИЕ ЧАСТИЦ, ЗАПОЛНЯЮЩИХ КРУГ
        // ==========================
        for (int i = 0; i < particleCount; i++) {
            // Генерируем случайную точку внутри круга
            double angle = Math.random() * Math.PI * 2;
            double distance = radius * Math.sqrt(Math.random()); // Равномерное распределение по площади
            
            double px = cx + Math.cos(angle) * distance;
            double py = cy + Math.sin(angle) * distance;

            // Размер частицы зависит от расстояния от центра (опционально)
            double particleSize = 1.5 + Math.random() * 3;
            
            Circle particle = new Circle(particleSize, color);
            particle.setCenterX(px);
            particle.setCenterY(py);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            // ==========================
            // 📐 ФИЗИКА ПАДЕНИЯ
            // ==========================
            
            // Близкие к центру частицы падают немного медленнее
            double fallDuration = 600 + Math.random() * 400 + (distance / radius) * 200;
            
            // Случайное смещение в стороны (больше для дальних частиц)
            double horizontalShift = (Math.random() - 0.5) * 40 * (distance / radius);
            
            // Вертикальное падение с небольшими вариациями
            double verticalFall = 60 + Math.random() * 40 + (distance / radius) * 20;

            TranslateTransition fall = new TranslateTransition(Duration.millis(fallDuration), particle);
            fall.setByX(horizontalShift);
            fall.setByY(verticalFall);
            fall.setInterpolator(Interpolator.EASE_IN); // Эффект ускорения

            // Вращение при падении
            RotateTransition rotate = new RotateTransition(Duration.millis(fallDuration), particle);
            rotate.setByAngle(90 + Math.random() * 180);
            rotate.setInterpolator(Interpolator.LINEAR);

            // Затухание
            FadeTransition fade = new FadeTransition(Duration.millis(fallDuration), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ParallelTransition anim = new ParallelTransition(fall, rotate, fade);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            
            // Небольшая случайная задержка для естественности
            anim.setDelay(Duration.millis(Math.random() * 50));
            anim.play();
        }

        // ==========================
        // 💫 ДОПОЛНИТЕЛЬНЫЕ ЧАСТИЦЫ ДЛЯ КРАЕВ
        // ==========================
        createEdgeParticles(root, cx, cy, radius, color);

        if (onFinish != null) onFinish.run();
    }

    // ==========================
    // 🌟 ЧАСТИЦЫ ДЛЯ КОНТУРА КРУГА
    // ==========================
    private static void createEdgeParticles(Pane root, double cx, double cy, double radius, Paint color) {
        int edgeParticles = (int)(radius * 2); // Количество частиц по контуру
        
        for (int i = 0; i < edgeParticles; i++) {
            double angle = Math.random() * Math.PI * 2;
            double px = cx + Math.cos(angle) * radius;
            double py = cy + Math.sin(angle) * radius;

            Circle edgeParticle = new Circle(1 + Math.random() * 2, color);
            edgeParticle.setCenterX(px);
            edgeParticle.setCenterY(py);
            edgeParticle.setMouseTransparent(true);

            root.getChildren().add(edgeParticle);

            // Частицы с контура падают немного иначе
            double fallDuration = 500 + Math.random() * 300;
            double horizontalShift = (Math.random() - 0.5) * 60;
            double verticalFall = 80 + Math.random() * 40;

            TranslateTransition fall = new TranslateTransition(Duration.millis(fallDuration), edgeParticle);
            fall.setByX(horizontalShift);
            fall.setByY(verticalFall);
            fall.setInterpolator(Interpolator.EASE_IN);

            FadeTransition fade = new FadeTransition(Duration.millis(fallDuration), edgeParticle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ParallelTransition anim = new ParallelTransition(fall, fade);
            anim.setOnFinished(e -> root.getChildren().remove(edgeParticle));
            anim.setDelay(Duration.millis(20 + Math.random() * 50));
            anim.play();
        }
    }

    // =========================
    // 🎨 ВАРИАНТ С РАЗНЫМИ ЦВЕТАМИ ЧАСТИЦ
    // =========================
    public static void playColorfulAreaFall(Pane root, Circle circle, Runnable onFinish) {
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double radius = circle.getRadius();
        Paint baseColor = circle.getFill();

        root.getChildren().remove(circle);

        // Создаем цветовую палитру на основе исходного цвета
        Color[] colorPalette = createColorPalette(baseColor);

        double area = Math.PI * radius * radius;
        int particleCount = Math.max(15, (int)(area / 12));

        for (int i = 0; i < particleCount; i++) {
            double angle = Math.random() * Math.PI * 2;
            double distance = radius * Math.sqrt(Math.random());
            
            double px = cx + Math.cos(angle) * distance;
            double py = cy + Math.sin(angle) * distance;

            // Выбираем случайный цвет из палитры
            Paint particleColor = colorPalette[i % colorPalette.length];
            
            Circle particle = new Circle(2 + Math.random() * 2.5, particleColor);
            particle.setCenterX(px);
            particle.setCenterY(py);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            // Физика падения
            double fallDuration = 700 + Math.random() * 500;
            double horizontalShift = (Math.random() - 0.5) * 50;
            double verticalFall = 70 + Math.random() * 50;

            TranslateTransition fall = new TranslateTransition(Duration.millis(fallDuration), particle);
            fall.setByX(horizontalShift);
            fall.setByY(verticalFall);
            fall.setInterpolator(Interpolator.EASE_IN);

            RotateTransition rotate = new RotateTransition(Duration.millis(fallDuration), particle);
            rotate.setByAngle(120 + Math.random() * 240);

            FadeTransition fade = new FadeTransition(Duration.millis(fallDuration), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            ParallelTransition anim = new ParallelTransition(fall, rotate, fade);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            anim.setDelay(Duration.millis(Math.random() * 80));
            anim.play();
        }

        if (onFinish != null) onFinish.run();
    }

    // ==========================
    // 🎨 СОЗДАНИЕ ЦВЕТОВОЙ ПАЛИТРЫ
    // ==========================
    private static Color[] createColorPalette(Paint basePaint) {
        if (basePaint instanceof Color) {
            Color baseColor = (Color) basePaint;
            return new Color[]{
                baseColor,
                baseColor.brighter(),
                baseColor.deriveColor(0, 1.0, 1.2, 1.0), // Более насыщенный
                baseColor.deriveColor(30, 1.0, 1.1, 1.0), // Слегка другой оттенок
                baseColor.deriveColor(-30, 1.0, 1.1, 1.0) // Другой оттенок
            };
        }
        // Если цвет не распознан, используем серую палитру
        return new Color[]{
            Color.GRAY, Color.LIGHTGRAY, Color.DARKGRAY, Color.WHITESMOKE
        };
    }
    // =========================
    // 📉 ЭКОНОМНЫЙ ВАРИАНТ: БЕЛЫЙ КОНТУР
    // =========================
    public static void playContourCollapse(Pane root, Circle circle, Runnable onFinish) {
        // Блокируем старый круг
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double radius = circle.getRadius();

        // Удаляем исходный цветной круг
        root.getChildren().remove(circle);

        // Ограничиваем количество частиц (макс 40), чтобы не лагало
        int particleCount = Math.min((int) (radius * 1.5), 40);
        double angleStep = (Math.PI * 2) / particleCount;

        for (int i = 0; i < particleCount; i++) {
            double angle = (i * angleStep);
            
            // Координаты на границе круга
            double px = cx + Math.cos(angle) * radius;
            double py = cy + Math.sin(angle) * radius;

            // ✅ ИЗМЕНЕНИЕ ЗДЕСЬ: Ставим Color.WHITE вместо переменной color
            // Размер частицы: от 2 до 4 пикселей
            Circle particle = new Circle(2 + Math.random() * 2, Color.WHITE);
            
            particle.setCenterX(px);
            particle.setCenterY(py);
            particle.setMouseTransparent(true);

            root.getChildren().add(particle);

            // ==========================
            // ФИЗИКА ОСЫПАНИЯ
            // ==========================
            double fallDuration = 500 + Math.random() * 400;
            
            // Небольшой разлет в стороны
            double horizontalShift = Math.cos(angle) * (5 + Math.random() * 15);
            // Падение вниз
            double verticalFall = 50 + Math.random() * 50;

            TranslateTransition fall = new TranslateTransition(Duration.millis(fallDuration), particle);
            fall.setByX(horizontalShift);
            fall.setByY(verticalFall);
            fall.setInterpolator(Interpolator.EASE_IN);

            FadeTransition fade = new FadeTransition(Duration.millis(fallDuration), particle);
            fade.setFromValue(1);
            fade.setToValue(0);

            // Немного уменьшаем их пока падают
            ScaleTransition scale = new ScaleTransition(Duration.millis(fallDuration), particle);
            scale.setToX(0.5);
            scale.setToY(0.5);

            ParallelTransition anim = new ParallelTransition(fall, fade, scale);
            anim.setOnFinished(e -> root.getChildren().remove(particle));
            
            // Случайная задержка для эффекта "сыпучести"
            anim.setDelay(Duration.millis(Math.random() * 100));
            anim.play();
        }

        if (onFinish != null) onFinish.run();
    }

    // =========================
    // ⚡ СУПЕР-ЛЕГКИЙ ВАРИАНТ: ИМПУЛЬС (1 НОДА)
    // =========================
    // Используйте это, если ПК совсем слабый. Создается всего 1 объект.
    public static void playFastShockwave(Pane root, Circle circle, Runnable onFinish) {
        circle.setMouseTransparent(true);
        circle.setOnMouseEntered(null);

        double cx = circle.getCenterX();
        double cy = circle.getCenterY();
        double radius = circle.getRadius();
        Paint color = circle.getFill();

        root.getChildren().remove(circle);

        // Создаем "призрак" круга (только контур)
        Circle shockwave = new Circle(radius);
        shockwave.setCenterX(cx);
        shockwave.setCenterY(cy);
        shockwave.setFill(Color.TRANSPARENT);
        shockwave.setStroke(color);
        shockwave.setStrokeWidth(4);
        shockwave.setMouseTransparent(true);

        root.getChildren().add(shockwave);

        // Быстрое расширение и исчезновение
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), shockwave);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.5);
        scale.setToY(1.5);

        FadeTransition fade = new FadeTransition(Duration.millis(300), shockwave);
        fade.setFromValue(1);
        fade.setToValue(0);

        // Можно добавить вращение для динамики
        RotateTransition rotate = new RotateTransition(Duration.millis(300), shockwave);
        rotate.setByAngle(45);

        ParallelTransition anim = new ParallelTransition(scale, fade, rotate);
        anim.setOnFinished(e -> {
            root.getChildren().remove(shockwave);
            if (onFinish != null) onFinish.run();
        });
        anim.play();
    }
}
