package com.example.move_arm.service;

import java.util.Collections;
import java.util.List;

import com.example.move_arm.database.GameResultDao;
import com.example.move_arm.model.GameResult;
import com.example.move_arm.model.User;

public class ResultService {

    private final GameResultDao gameResultDao = new GameResultDao();

    /**
     * Получить результаты конкретного пользователя
     */
    public List<GameResult> getResultsForUser(User user) {

        if (user == null) {
            return Collections.emptyList();
        }

        return gameResultDao.findByUserId(user.getId());
    }

    /**
     * Получить все результаты в базе
     */
    public List<GameResult> getAllResults() {
        return gameResultDao.findAll();
    }

    /**
     * Удалить все результаты пользователя
     */
    public void deleteResultsForUser(User user) {

        if (user == null) {
            return;
        }

        gameResultDao.deleteByUserId(user.getId());
    }
}