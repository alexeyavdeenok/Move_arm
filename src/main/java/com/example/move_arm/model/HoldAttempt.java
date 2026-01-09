package com.example.move_arm.model;

public class HoldAttempt {

    private final int attemptIndex;
    private final long startTimeNs;
    private final long endTimeNs;
    private final long requiredHoldMs;
    private final long actualHoldMs;
    private final boolean success;

    // ✅ ОСНОВНОЙ конструктор (оставляем как есть)
    public HoldAttempt(
            int attemptIndex,
            long startTimeNs,
            long endTimeNs,
            long requiredHoldMs,
            long actualHoldMs,
            boolean success
    ) {
        this.attemptIndex = attemptIndex;
        this.startTimeNs = startTimeNs;
        this.endTimeNs = endTimeNs;
        this.requiredHoldMs = requiredHoldMs;
        this.actualHoldMs = actualHoldMs;
        this.success = success;
    }

    // ✅ НОВЫЙ конструктор — ДЛЯ ТЕКУЩЕГО КОДА
    public HoldAttempt(
            long startTimeNs,
            long endTimeNs,
            long requiredHoldMs,
            long actualHoldMs,
            boolean success
    ) {
        this(
            -1, // attemptIndex пока не используем
            startTimeNs,
            endTimeNs,
            requiredHoldMs,
            actualHoldMs,
            success
        );
    }

    public int getAttemptIndex() { return attemptIndex; }
    public long getStartTimeNs() { return startTimeNs; }
    public long getEndTimeNs() { return endTimeNs; }
    public long getRequiredHoldMs() { return requiredHoldMs; }
    public long getActualHoldMs() { return actualHoldMs; }
    public boolean isSuccess() { return success; }
}
