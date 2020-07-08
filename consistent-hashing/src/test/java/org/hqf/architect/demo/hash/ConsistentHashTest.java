package org.hqf.architect.demo.hash;

import org.hqf.architect.demo.hash.stat.MurmurHashStrategy;
import org.junit.Test;

import java.util.*;


public class ConsistentHashTest {

    private static final String prefix = "D";

    private static final int maxKeyCount = 1000000;



    @Test
    public void test() {

        Set<String> nodes = new HashSet<String>();
        for (int i = 1; i <= 10; i++) {
            nodes.add(String.format("192.168.1.%d", i));
        }

        ConsistentHash<String> consistentHash = new ConsistentHash<String>(20, nodes, new MurmurHashStrategy());

        System.out.println("hash circle size: " + consistentHash.getSize());

        for (Map.Entry<Long, String> entry : consistentHash.getMap().entrySet()) {
            System.out.println("entry = " + entry);
        }


        HashMap<String, Integer> statisticMap = new HashMap<>();
        for (long i = 0; i < maxKeyCount; i++) {
            String node = consistentHash.getNode(prefix + i);
//            System.out.println("node----------->:" + node);

            Integer sum = 0;

            if (statisticMap.containsKey(node)) {
                sum = statisticMap.get(node);
            } else {
                statisticMap.put(node, sum);
            }
            statisticMap.put(node, sum + 1);
        }


        System.out.println(" ============================ ");
        for (Map.Entry<String,Integer > integerStringEntry : statisticMap.entrySet()) {
            System.out.println("keyValue = " + integerStringEntry);
        }

        PerfUtil.analyze(statisticMap, maxKeyCount, nodes.size());


    }

}