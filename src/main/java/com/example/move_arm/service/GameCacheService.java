package com.example.move_arm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.move_arm.model.ClickData;
import com.example.move_arm.model.HoldAttempt;

public class GameCacheService {

    private List<ClickData> lastGameClicks = new ArrayList<>();
    private List<HoldAttempt> lastHoldAttempts = new ArrayList<>();

    public void storeClicks(List<ClickData> clicks) {
        lastGameClicks = new ArrayList<>(clicks);
    }

    public List<ClickData> getLastGameClicks() {
        return Collections.unmodifiableList(lastGameClicks);
    }

    public void storeHoldAttempts(List<HoldAttempt> attempts) {
        lastHoldAttempts = new ArrayList<>(attempts);
    }

    public List<HoldAttempt> getLastHoldAttempts() {
        return Collections.unmodifiableList(lastHoldAttempts);
    }

    public void clear() {
        lastGameClicks.clear();
        lastHoldAttempts.clear();
    }
}
