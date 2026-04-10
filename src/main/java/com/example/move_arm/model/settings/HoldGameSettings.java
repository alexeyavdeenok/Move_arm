package com.example.move_arm.model.settings;

public class HoldGameSettings {

    private int radius = 30;
    private int holdTimeMs = 0; // пока 0
    private int durationSeconds = 60;
    private int maxCirclesCount = 3;

    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; }

    public int getHoldTimeMs() { return holdTimeMs; }
    public void setHoldTimeMs(int holdTimeMs) { this.holdTimeMs = holdTimeMs; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public int getMaxCirclesCount() { return maxCirclesCount; }
    public void setMaxCirclesCount(int maxCirclesCount) { this.maxCirclesCount = maxCirclesCount; }

}