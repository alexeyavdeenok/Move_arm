package com.example.move_arm.ui.view;

public record TargetHitEvent(
    double cursorX,
    double cursorY,
    double targetX,
    double targetY,
    int radius,
    long relativeTimeNs
) {}