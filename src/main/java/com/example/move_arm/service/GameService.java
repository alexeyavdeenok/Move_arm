package com.example.move_arm.service;

import java.util.List;

import com.example.move_arm.model.ClickData;
import com.example.move_arm.model.GameResult;
import com.example.move_arm.model.GameType;
import com.example.move_arm.model.HoldAttempt;
import com.example.move_arm.model.Statistics;
import com.example.move_arm.model.User;

public class GameService {

    private static final GameService INSTANCE = new GameService();

    private final UserService userService = UserService.getInstance();
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

    public int addGameClicks(int radius, int seed, List<ClickData> clicks) {

        cacheService.storeClicks(clicks);

        return clickGameService.saveClicks(
                userService.getCurrentUser().getId(),
                gameTypeService.getCurrentGameTypeId(),
                radius,
                seed,
                clicks
        );
    }

    public int addHoldGameResults(int radius, List<HoldAttempt> attempts) {
        int resultId = holdGameService.saveHoldResults(
                userService.getCurrentUser().getId(),
                gameTypeService.getCurrentGameTypeId(),
                radius,
                attempts
        );
        cacheService.storeHoldAttempts(attempts);
        return resultId;
    }

    public List<ClickData> getLastGameClicks() {
        return cacheService.getLastGameClicks();
    }

    public List<HoldAttempt> getLastHoldAttempts() {
        return cacheService.getLastHoldAttempts();
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