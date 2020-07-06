package org.hqf.architect.demo.hash;

import java.util.IntSummaryStatistics;
import java.util.Map;

public class PerfUtil {

    /**
     *
     * @param nodeCount
     * @param dataNum
     * @param nodeNum
     */
    public static void analyze(Map<String, Integer> nodeCount, int dataNum, int nodeNum) {

        //数据理论平均分布值
        double average = (double) dataNum / nodeNum;

        IntSummaryStatistics s1
                = nodeCount.values().stream().mapToInt(Integer::intValue).summaryStatistics();

        int max = s1.getMax();
        int min = s1.getMin();
        int range = max - min;
        double standardDeviation
                = nodeCount.values().stream()
                .mapToDouble(n -> Math.abs(n - average))
                .summaryStatistics()
                .getAverage();

        System.out.println(String.format("平均值：%.2f", average));
        System.out.println(String.format("最大值：%d,（%.2f%%）", max, 100.0 * max / average));
        System.out.println(String.format("最小值：%d,（%.2f%%）", min, 100.0 * min / average));
        System.out.println(String.format("极差：%d,（%.2f%%）", range, 100.0 * range / average));
        System.out.println(String.format("标准差：%.2f,（%.2f%%）", standardDeviation, 100.0 * standardDeviation / average));
    }

}
