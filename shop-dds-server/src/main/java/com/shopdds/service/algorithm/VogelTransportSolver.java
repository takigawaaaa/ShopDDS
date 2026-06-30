package com.shopdds.service.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Vogel 似近法（VAM）运输问题求解器
 * <p>
 * 复用自原 {@code util/vogel.java} 的 {@code TP_vogel()} 方法，仅保留纯算法，
 * 移除了原有的数据库访问与控制台打印。
 *
 * <p>求解目标：在供应量 a[]、需求量 b[]、运价矩阵 c[][] 给定时，求一个初始基本可行解 x[][]，
 * 使总运费尽量小。返回 m×n 的运量矩阵。
 */
@Slf4j
@Component
public class VogelTransportSolver {

    private static final double M = 1e9;

    /**
     * 注意：本方法会修改传入的 a、b 数组（按原算法语义），但不会修改 c。
     */
    public double[][] solve(double[][] c, double[] a, double[] b) {
        return TP_vogel(c, a, b);
    }

    /**
     * 原 vogel.TP_vogel 实现，保持算法语义不变。
     */
    public static double[][] TP_vogel(double[][] c, double[] a, double[] b) {
        int m = c.length;
        int n = c[0].length;
        double[][] cost = deepCopyMatrix(c);
        double[][] x = new double[m][n];

        while (true) {
            if (allElementsEqual(c, M)) break;

            // 行最小、次小
            double[] rowMini1 = new double[m];
            double[] rowMini2 = new double[m];
            for (int i = 0; i < m; i++) {
                double[] row = c[i].clone();
                Arrays.sort(row);
                rowMini1[i] = row[0];
                rowMini2[i] = (row.length > 1) ? row[1] : row[0];
            }
            double[] rPun = new double[m];
            for (int i = 0; i < m; i++) {
                rPun[i] = rowMini2[i] - rowMini1[i];
            }

            // 列最小、次小
            double[] colMini1 = new double[n];
            double[] colMini2 = new double[n];
            for (int j = 0; j < n; j++) {
                double[] col = new double[m];
                for (int i = 0; i < m; i++) {
                    col[i] = c[i][j];
                }
                Arrays.sort(col);
                colMini1[j] = col[0];
                colMini2[j] = (col.length > 1) ? col[1] : col[0];
            }
            double[] cPun = new double[n];
            for (int j = 0; j < n; j++) {
                cPun[j] = colMini2[j] - colMini1[j];
            }

            // 行列罚值合并，找最大罚值
            double[] pun = new double[m + n];
            System.arraycopy(rPun, 0, pun, 0, m);
            System.arraycopy(cPun, 0, pun, m, n);
            double maxPun = Arrays.stream(pun).max().getAsDouble();
            int maxPunIndex = 0;
            for (int i = 0; i < pun.length; i++) {
                if (pun[i] == maxPun) {
                    maxPunIndex = i;
                    break;
                }
            }

            if (maxPunIndex < m) {
                // 行罚值最大
                int rowIndex = maxPunIndex;
                double minVal = Double.MAX_VALUE;
                int minColIndex = -1;
                for (int j = 0; j < n; j++) {
                    if (c[rowIndex][j] < minVal) {
                        minVal = c[rowIndex][j];
                        minColIndex = j;
                    }
                }
                if (minColIndex != -1) {
                    if (a[rowIndex] <= b[minColIndex]) {
                        x[rowIndex][minColIndex] = a[rowIndex];
                        a[rowIndex] = 0;
                        b[minColIndex] -= x[rowIndex][minColIndex];
                        Arrays.fill(c[rowIndex], M);
                    } else {
                        x[rowIndex][minColIndex] = b[minColIndex];
                        b[minColIndex] = 0;
                        a[rowIndex] -= x[rowIndex][minColIndex];
                        for (int i = 0; i < m; i++) {
                            c[i][minColIndex] = M;
                        }
                    }
                }
            } else {
                // 列罚值最大
                int colIndex = maxPunIndex - m;
                if (colIndex < n) {
                    double minVal = Double.MAX_VALUE;
                    int minRowIndex = -1;
                    for (int i = 0; i < m; i++) {
                        if (c[i][colIndex] < minVal) {
                            minVal = c[i][colIndex];
                            minRowIndex = i;
                        }
                    }
                    if (minRowIndex != -1) {
                        if (a[minRowIndex] <= b[colIndex]) {
                            x[minRowIndex][colIndex] = a[minRowIndex];
                            a[minRowIndex] = 0;
                            b[colIndex] -= x[minRowIndex][colIndex];
                            Arrays.fill(c[minRowIndex], M);
                        } else {
                            x[minRowIndex][colIndex] = b[colIndex];
                            b[colIndex] = 0;
                            a[minRowIndex] -= x[minRowIndex][colIndex];
                            for (int i = 0; i < m; i++) {
                                c[i][colIndex] = M;
                            }
                        }
                    }
                }
            }
        }

        // 日志输出（替代原 System.out）
        double totalCost = 0;
        int varnum = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                totalCost += x[i][j] * cost[i][j];
                if (x[i][j] != 0) varnum++;
            }
        }
        log.debug("Vogel 求解完成：总成本={}, 基变量数={} (m+n-1={})", totalCost, varnum, m + n - 1);
        if (varnum != m + n - 1) {
            log.debug("【注意：问题含有退化解】");
        }

        return x;
    }

    public static boolean allElementsEqual(double[][] matrix, double value) {
        for (double[] row : matrix) {
            for (double elem : row) {
                if (elem != value) {
                    return false;
                }
            }
        }
        return true;
    }

    public static double[][] deepCopyMatrix(double[][] original) {
        if (original == null) {
            return null;
        }
        final double[][] result = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}