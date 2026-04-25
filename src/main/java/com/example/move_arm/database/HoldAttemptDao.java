package com.example.move_arm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.move_arm.model.HoldAttempt;

/**
 * DAO для работы с таблицей hold_attempts.
 * Отвечает за сохранение детальной статистики каждой попытки удержания.
 */
public class HoldAttemptDao {

    private final DatabaseManager dbManager = DatabaseManager.getInstance();

    /**
     * Пакетное сохранение списка попыток удержания в БД.
     * Используется транзакция (commit) для обеспечения целостности и скорости.
     *
     * @param resultId ID из таблицы game_results, к которому привязываются попытки.
     * @param attempts Список объектов HoldAttempt, накопленных за игру.
     */
    public void insertHoldAttempts(int resultId, List<HoldAttempt> attempts) {
        if (attempts == null || attempts.isEmpty()) {
            return;
        }

        String sql = """
            INSERT INTO holds (
                result_id, 
                attempt_index, 
                start_time_ns, 
                end_time_ns, 
                actual_hold_ms, 
                success, 
                target_center_x, 
                target_center_y
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dbManager.getConnection()) {
            // Отключаем автоматический коммит, чтобы выполнить всё одной транзакцией
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (HoldAttempt attempt : attempts) {
                    ps.setInt(1, resultId);
                    ps.setInt(2, attempt.getAttemptIndex());
                    ps.setLong(3, attempt.getStartTimeNs());
                    ps.setLong(4, attempt.getEndTimeNs());
                    ps.setLong(5, attempt.getActualHoldMs());
                    // Преобразуем boolean в 0 или 1 для SQLite
                    ps.setInt(6, attempt.isSuccess() ? 1 : 0);
                    ps.setDouble(7, attempt.getTargetCenterX());
                    ps.setDouble(8, attempt.getTargetCenterY());
                    
                    ps.addBatch();
                }

                // Выполняем всю пачку запросов разом
                ps.executeBatch();
                // Фиксируем изменения в базе
                conn.commit();
                
            } catch (Exception e) {
                // Если что-то пошло не так, откатываем изменения
                conn.rollback();
                throw e;
            } finally {
                // Возвращаем стандартное поведение соединения
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.err.println("HoldAttemptDao Error: Не удалось сохранить попытки игры.");
            e.printStackTrace();
        }
    }
    /**
     * Возвращает максимальное количество успешных удержаний для пользователя и радиуса.
     *
     * @param userId ID пользователя.
     * @param radius Радиус мишени.
     * @return Максимальное число успешных попыток (score) по всем играм.
     */
    public int getMaxSuccessForUserAndRadius(int userId, int radius) {
        String sql = """
            SELECT MAX(success_count) AS record FROM (
                SELECT COUNT(*) AS success_count
                FROM holds h
                JOIN game_results g ON h.result_id = g.id
                WHERE g.user_id = ? AND g.radius = ? AND h.success = 1
                GROUP BY h.result_id
            )
            """;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, radius);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("record");
                }
            }
        } catch (Exception e) {
            System.err.println("HoldAttemptDao Error: Не удалось получить рекорд для userId=" + userId + ", radius=" + radius);
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Считывает все попытки удержания для конкретной игры из базы данных.
     *
     * @param resultId ID игры из таблицы game_results.
     * @return Список объектов HoldAttempt, отсортированный по порядку их совершения.
     */
    public List<HoldAttempt> readAttemptsForResult(int resultId) {
        List<HoldAttempt> attempts = new ArrayList<>();
        
        // Сортировка по attempt_index критически важна для правильного графика
        String sql = """
            SELECT attempt_index, start_time_ns, end_time_ns, 
                actual_hold_ms, success, target_center_x, target_center_y 
            FROM holds 
            WHERE result_id = ? 
            ORDER BY attempt_index ASC
        """;

        try (Connection conn = dbManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, resultId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoldAttempt attempt = new HoldAttempt(
                        rs.getInt("attempt_index"),
                        rs.getLong("start_time_ns"),
                        rs.getLong("end_time_ns"),
                        rs.getLong("actual_hold_ms"),
                        rs.getInt("success") == 1, // Конвертируем 1/0 обратно в boolean
                        rs.getDouble("target_center_x"),
                        rs.getDouble("target_center_y")
                    );
                    attempts.add(attempt);
                }
            }
        } catch (Exception e) {
            System.err.println("HoldAttemptDao Error: Не удалось прочитать попытки для resultId=" + resultId);
            e.printStackTrace();
        }
        
        return attempts;
    }
}