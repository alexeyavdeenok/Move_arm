package com.example.move_arm.model.settings;

import com.example.move_arm.model.AnimationType;

// Этот класс готов стать @MappedSuperclass в JPA
public abstract class BaseSettings {
    
    // @Id
    // @GeneratedValue
    private Long id;
    private int durationSeconds = 30;
    private int maxCirclesCount = 3;
    private int minRadius = 20;
    private int maxRadius = 50;
    private int radius = 30;
    private int seed = 67;
    private AnimationType animationType = AnimationType.CONTOUR_COLLAPSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRadius() {return radius;}
    public void setRadius(int radius) {this.radius = radius;}

    // Геттеры и Сеттеры (ORM использует их для маппинга)
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getMaxCirclesCount() { return maxCirclesCount; }
    public void setMaxCirclesCount(int maxCirclesCount) { this.maxCirclesCount = maxCirclesCount; }

    public double getMinRadius() { return minRadius; }
    public void setMinRadius(int minRadius) { this.minRadius = minRadius; }

    public double getMaxRadius() { return maxRadius; }
    public void setMaxRadius(int maxRadius) { this.maxRadius = maxRadius; }

    public AnimationType getAnimationType() { return animationType; }
    public void setAnimationType(AnimationType animationType) { this.animationType = animationType; }

    public int getSeed(){ return seed; }
    public void setSeed(int seed){ this.seed = seed;}
}