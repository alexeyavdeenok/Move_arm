package com.example.move_arm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.move_arm.model.settings.HoldGameSettings;

public class HoldSettingsDao {

    private final DatabaseManager db = DatabaseManager.getInstance();

    public HoldGameSettings load(long userId) {

        String sql = "SELECT * FROM hold_settings WHERE user_id = ?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HoldGameSettings s = new HoldGameSettings();

                s.setRadius(rs.getInt("radius"));
                s.setHoldTimeMs(rs.getInt("hold_time_ms"));

                return s;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new HoldGameSettings(); // дефолт
    }

    public void save(long userId, HoldGameSettings settings) {

        String sql = """
            INSERT INTO hold_settings(
                user_id,
                radius,
                hold_time_ms
            )
            VALUES (?, ?, ?)
            ON CONFLICT(user_id) DO UPDATE SET
                radius = excluded.radius,
                hold_time_ms = excluded.hold_time_ms
        """;

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setInt(2, settings.getRadius());
            ps.setInt(3, settings.getHoldTimeMs());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}