package com.shopdds.service.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vogel 似近法求解器单元测试
 * <p>
 * 用一个已知运输问题验证算法正确性：
 * 2 个供应点(供应 50, 70)，3 个需求点(需求 30, 40, 50)
 * 运价矩阵给出，验证：1) 供需平衡 2) 调拨量非负 3) 不超供需
 */
class VogelTransportSolverTest {

    private final VogelTransportSolver solver = new VogelTransportSolver();

    @Test
    void solveBalancedTransportProblem() {
        // 供应：仓库0=50, 仓库1=70；总供给 120
        double[] supplies = {50, 70};
        // 需求：超市0=30, 超市1=40, 超市2=50；总需求 120
        double[] demands = {30, 40, 50};
        // 运价（距离）矩阵 [m][n]
        double[][] costs = {
                {3.0, 5.0, 7.0},
                {6.0, 2.0, 4.0}
        };

        double[][] x = solver.solve(costs, supplies.clone(), demands.clone());

        // 行数 = 供应点数，列数 = 需求点数
        assertEquals(2, x.length);
        assertEquals(3, x[0].length);

        // 各超市收货量 == 需求量
        for (int j = 0; j < demands.length; j++) {
            double colSum = 0;
            for (int i = 0; i < supplies.length; i++) colSum += x[i][j];
            assertEquals(demands[j], colSum, 1e-6, "超市" + j + "收货量应等于需求");
        }
        // 各仓库发货量 <= 供应量
        for (int i = 0; i < supplies.length; i++) {
            double rowSum = 0;
            for (int j = 0; j < demands.length; j++) rowSum += x[i][j];
            assertTrue(rowSum <= supplies[i] + 1e-6, "仓库" + i + "发货量不应超过供应");
        }
        // 运量非负
        for (double[] row : x) for (double v : row) assertTrue(v >= -1e-9);
    }

    @Test
    void degenerateCaseIsHandled() {
        // 供应略大于需求，验证算法不会抛异常
        double[] supplies = {20, 30};
        double[] demands = {10, 10, 10};
        double[][] costs = {{1, 2, 3}, {4, 5, 6}};

        double[][] x = solver.solve(costs, supplies.clone(), demands.clone());

        for (int j = 0; j < demands.length; j++) {
            double colSum = 0;
            for (int i = 0; i < supplies.length; i++) colSum += x[i][j];
            assertEquals(demands[j], colSum, 1e-6);
        }
    }
}