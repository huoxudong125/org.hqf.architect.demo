package org.hqf.architect.demo.hash;

import java.util.*;

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

    /*
     * 查看表示整个哈希环中各个虚拟节点位置
     */
    public static void testBalance(Map  circle) {
        Set<Integer> sets = circle.keySet();//获得TreeMap中所有的Key
        SortedSet<Integer> sortedSets = new TreeSet<Integer>(sets);//将获得的Key集合排序
        for (Integer hashCode : sortedSets) {
            System.out.println(hashCode);
        }

        System.out.println("----each location 's distance are follows: ----");
        /*
         * 查看相邻两个hashCode的差值
         */
        Iterator<Integer> it = sortedSets.iterator();
        Iterator<Integer> it2 = sortedSets.iterator();
        if (it2.hasNext())
            it2.next();
        long keyPre, keyAfter;
        while (it.hasNext() && it2.hasNext()) {
            keyPre = it.next();
            keyAfter = it2.next();
            System.out.println(keyAfter - keyPre);
        }
    }

}
