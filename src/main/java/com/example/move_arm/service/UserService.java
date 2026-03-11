package com.example.move_arm.service;

import java.util.Optional;

import com.example.move_arm.database.DatabaseManager;
import com.example.move_arm.database.UserDao;
import com.example.move_arm.model.User;

public class UserService {

    private final UserDao userDao = new UserDao();
    private final DatabaseManager dbManager = DatabaseManager.getInstance();

    private User currentUser;

    public UserService() {
        restoreLastUser();
    }

    private void restoreLastUser() {
        try {
            String lastUserId = dbManager.getAppProperty("last_user_id");

            if (lastUserId != null) {
                int id = Integer.parseInt(lastUserId);
                Optional<User> user = userDao.findById(id);
                if (user.isPresent()) {
                    currentUser = user.get();
                    return;
                }
            }

        } catch (Exception ignored) {}

        Optional<User> guest = userDao.findByUsername("guest");
        currentUser = guest.orElseGet(() -> userDao.createUser("guest"));

        saveLastUser();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        if (user == null) return;
        currentUser = user;
        saveLastUser();
    }

    private void saveLastUser() {
        try {
            dbManager.setAppProperty("last_user_id", String.valueOf(currentUser.getId()));
        } catch (Exception ignored) {}
    }
}