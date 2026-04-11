package com.example.move_arm.model.settings;

import com.example.move_arm.model.AnimationType;

public class GlobalSettings {

    private AnimationType animationType = AnimationType.CONTOUR_COLLAPSE;

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }
}