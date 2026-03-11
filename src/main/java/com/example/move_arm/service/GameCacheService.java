package com.example.move_arm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.move_arm.model.ClickData;

public class GameCacheService {

    private List<ClickData> lastGameClicks = new ArrayList<>();

    public void storeClicks(List<ClickData> clicks) {
        lastGameClicks = new ArrayList<>(clicks);
    }

    public List<ClickData> getLastGameClicks() {
        return Collections.unmodifiableList(lastGameClicks);
    }

    public void clear() {
        lastGameClicks.clear();
    }
}