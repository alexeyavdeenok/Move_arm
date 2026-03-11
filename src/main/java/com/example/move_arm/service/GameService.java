package com.example.move_arm.service;

import java.util.List;

import com.example.move_arm.model.*;

public class GameService {

    private static final GameService INSTANCE = new GameService();

    private final UserService userService = new UserService();
    private final GameTypeService gameTypeService = new GameTypeService();
    private final ClickGameService clickGameService = new ClickGameService();
    private final HoldGameService holdGameService = new HoldGameService();
    private final GameCacheService cacheService = new GameCacheService();
    private final ResultService resultService = new ResultService();

    public static GameService getInstance() {
        return INSTANCE;
    }

    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    public void setCurrentUser(User user) {
        userService.setCurrentUser(user);
    }

    public void setCurrentGameType(GameType type) {
        gameTypeService.setCurrentGameType(type);
    }

    public int getCurrentGameTypeId() {
        return gameTypeService.getCurrentGameTypeId();
    }

    public String getCurrentGameTypeString() {
        return gameTypeService.getCurrentGameTypeString();
    }

    public int addGameClicks(int radius, List<ClickData> clicks) {

        cacheService.storeClicks(clicks);

        return clickGameService.saveClicks(
                userService.getCurrentUser().getId(),
                gameTypeService.getCurrentGameTypeId(),
                radius,
                clicks
        );
    }

    public int addHoldGameResults(int radius, List<HoldAttempt> attempts) {

        return holdGameService.saveHoldResults(
                userService.getCurrentUser().getId(),
                gameTypeService.getCurrentGameTypeId(),
                radius,
                attempts
        );
    }

    public List<ClickData> getLastGameClicks() {
        return cacheService.getLastGameClicks();
    }

    public void printLastGameSummary() {

        List<ClickData> last = cacheService.getLastGameClicks();

        System.out.println("СТАТИСТИКА ПОСЛЕДНЕЙ ИГРЫ:");
        System.out.println(Statistics.getSummary(last));
    }

    public List<GameResult> getResultsForCurrentUser() {

        return resultService.getResultsForUser(
                userService.getCurrentUser()
        );
    }

    public void clear() {
        cacheService.clear();
    }
}