package com.example.move_arm.model;

public class HoldAttempt {
    private final int attemptIndex;
    private final long startTimeNs;
    private final long endTimeNs;
    private final long actualHoldMs;
    private final boolean success;
    private final double targetCenterX;
    private final double targetCenterY;

    public HoldAttempt(int attemptIndex, long startNs, long endNs, 
                       long actualMs, boolean success, double tx, double ty) {
        this.attemptIndex = attemptIndex;
        this.startTimeNs = startNs;
        this.endTimeNs = endNs;
        this.actualHoldMs = actualMs;
        this.success = success;
        this.targetCenterX = tx;
        this.targetCenterY = ty;
    }

    // Геттеры для DAO...
    public int getAttemptIndex() { return attemptIndex; }
    public long getStartTimeNs() { return startTimeNs; }
    public long getEndTimeNs() { return endTimeNs; }
    public long getActualHoldMs() { return actualHoldMs; }
    public boolean isSuccess() { return success; }
    public double getTargetCenterX() { return targetCenterX; }
    public double getTargetCenterY() { return targetCenterY; }
}