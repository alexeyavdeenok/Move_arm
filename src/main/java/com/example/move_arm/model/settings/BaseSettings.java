package com.example.move_arm.model.settings;

public abstract class BaseSettings {
    private int durationSeconds;
    private int maxCirclesCount;
    private int radius;
    private int seed;

    public BaseSettings(){}

    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; }

    public int getDurationSeconds() {return this.durationSeconds;}
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getSeed(){return this.seed;}
    public void setSeed(int seed) {this.seed = seed;}

    public int getMaxCirclesCount() { return maxCirclesCount; }
    public void setMaxCirclesCount(int maxCirclesCount) { this.maxCirclesCount = maxCirclesCount; }



}
