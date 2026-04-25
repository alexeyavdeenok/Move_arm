package com.example.move_arm.comtroller;

import com.example.move_arm.model.settings.BaseSettings;

public class HoverSettingsController extends BaseSettingsController {

    @Override
    protected BaseSettings getGameSettings() {
        return settingsService.getHoverSettings();
    }

    @Override
    protected void initializeSpecific() {
        // У hover-игры нет дополнительных уникальных параметров
    }

    @Override
    protected void saveSpecificSettings() {
        // Дополнительных полей нет
    }
}