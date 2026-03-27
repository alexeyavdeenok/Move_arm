package com.example.move_arm.service;

import java.util.List;
import java.util.Random;

public class WeightedGridGenerator implements PointGenerator {

    private final Random random = new Random();

    private int gridCols;
    private int gridRows;
    private double cellSize;

    private double[][] weights;
    private double[][] avoidScore;
    private long[][] lastUsed;

    private boolean initialized = false;

    private double minDistanceBetweenCircles = 150.0;

    // --- для анализа поведения ---
    private List<double[]> previousTargets = null;
    private double[] previousLastHit = null;

    // ================== MAIN ==================

    @Override
    public double[] nextPoint(double width, double height, int radius, List<double[]> activePoints) {

        if (!initialized) {
            initialize(width, height, radius);
        }

        // анализ поведения пользователя
        updateFromUserChoice(activePoints);

        decay();

        boolean[][] occupied = getOccupiedCells(activePoints);

        int[] cell = pickCell(occupied);

        if (cell != null) {
            int gx = cell[0];
            int gy = cell[1];

            lastUsed[gx][gy] = System.currentTimeMillis();
            weights[gx][gy] *= 0.7; // уменьшаем шанс повтора

            return generateInCell(gx, gy, radius);
        }

        // fallback
        return new double[]{
                radius + random.nextDouble() * (width - 2 * radius),
                radius + random.nextDouble() * (height - 2 * radius)
        };
    }

    // ================== INIT ==================

    private void initialize(double width, double height, int radius) {

        cellSize = 2 * radius + minDistanceBetweenCircles;

        gridCols = Math.max(1, (int)(width / cellSize));
        gridRows = Math.max(1, (int)(height / cellSize));

        weights = new double[gridCols][gridRows];
        avoidScore = new double[gridCols][gridRows];
        lastUsed = new long[gridCols][gridRows];

        long now = System.currentTimeMillis();

        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
                weights[x][y] = 1.0;
                avoidScore[x][y] = 0.0;
                lastUsed[x][y] = now;
            }
        }

        initialized = true;
    }

    // ================== USER ANALYSIS ==================

    private void updateFromUserChoice(List<double[]> activePoints) {

        if (activePoints == null || activePoints.isEmpty()) return;

        double[] currentLastHit = activePoints.get(activePoints.size() - 1);

        if (previousTargets == null) {
            previousTargets = activePoints;
            previousLastHit = currentLastHit;
            return;
        }

        for (double[] p : previousTargets) {

            int gx = getCellX(p[0]);
            int gy = getCellY(p[1]);

            if (samePoint(p, previousLastHit)) {
                // выбранная цель → уменьшаем вероятность
                weights[gx][gy] *= 0.5;
                avoidScore[gx][gy] -= 0.3;
            } else {
                // проигнорированная → увеличиваем шанс
                avoidScore[gx][gy] += 0.6;
            }
        }

        previousTargets = activePoints;
        previousLastHit = currentLastHit;
    }

    private boolean samePoint(double[] a, double[] b) {
        return Math.abs(a[0] - b[0]) < 1 && Math.abs(a[1] - b[1]) < 1;
    }

    // ================== PICK CELL ==================

    private int[] pickCell(boolean[][] occupied) {

        double total = 0;
        double[][] effective = new double[gridCols][gridRows];

        long now = System.currentTimeMillis();

        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {

                if (occupied[x][y]) continue;

                double timeBonus = (now - lastUsed[x][y]) * 0.0001;

                double w =
                        weights[x][y]
                      + timeBonus
                      + avoidScore[x][y] * 0.7;

                w = Math.max(0.1, Math.min(5.0, w));

                effective[x][y] = w;
                total += w;
            }
        }

        if (total == 0) return null;

        double r = random.nextDouble() * total;
        double sum = 0;

        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {
                sum += effective[x][y];
                if (r <= sum) {
                    return new int[]{x, y};
                }
            }
        }

        return null;
    }

    // ================== GENERATION ==================

    private double[] generateInCell(int gx, int gy, int radius) {

        double cellX = gx * cellSize;
        double cellY = gy * cellSize;

        double margin = radius;

        double minX = cellX + margin;
        double maxX = cellX + cellSize - margin;

        double minY = cellY + margin;
        double maxY = cellY + cellSize - margin;

        double x = minX + random.nextDouble() * (maxX - minX);
        double y = minY + random.nextDouble() * (maxY - minY);

        return new double[]{x, y};
    }

    // ================== HELPERS ==================

    private int getCellX(double x) {
        return Math.min((int)(x / cellSize), gridCols - 1);
    }

    private int getCellY(double y) {
        return Math.min((int)(y / cellSize), gridRows - 1);
    }

    private boolean[][] getOccupiedCells(List<double[]> activePoints) {
        boolean[][] occupied = new boolean[gridCols][gridRows];

        for (double[] p : activePoints) {
            int gx = getCellX(p[0]);
            int gy = getCellY(p[1]);
            occupied[gx][gy] = true;
        }

        return occupied;
    }

    // ================== DECAY ==================

    private void decay() {
        for (int x = 0; x < gridCols; x++) {
            for (int y = 0; y < gridRows; y++) {

                weights[x][y] *= 0.995;
                avoidScore[x][y] *= 0.99;

                weights[x][y] = Math.max(0.1, Math.min(3.0, weights[x][y]));
                avoidScore[x][y] = Math.max(-2.0, Math.min(2.0, avoidScore[x][y]));
            }
        }
    }
}