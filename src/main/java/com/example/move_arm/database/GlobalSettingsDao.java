package com.example.move_arm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.move_arm.model.AnimationType;
import com.example.move_arm.model.settings.GlobalSettings;

public class GlobalSettingsDao {

    private final DatabaseManager db = DatabaseManager.getInstance();

    public GlobalSettings load(long userId) {

        String sql = "SELECT * FROM global_settings WHERE user_id = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                GlobalSettings s = new GlobalSettings();
                s.setAnimationType(AnimationType.valueOf(rs.getString("animation_type")));
                return s;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new GlobalSettings(); // дефолт
    }

    public void save(long userId, GlobalSettings settings) {

        String sql = """
            INSERT INTO global_settings(user_id, animation_type)
            VALUES (?, ?)
            ON CONFLICT(user_id) DO UPDATE SET
                animation_type = excluded.animation_type
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, settings.getAnimationType().name());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}