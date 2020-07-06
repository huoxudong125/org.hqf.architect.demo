package org.hqf.architect.demo.hash;

import org.junit.Test;

import java.util.*;


public class ConsistentHashTest {

    private static final String prefix = "D";

    private static final long maxKeyCount = 1000000;



    @Test
    public void test() {

        Random random=new Random();

        Set<String> nodes = new HashSet<String>();
        for (int i = 1; i <= 10; i++) {
            nodes.add(String.format("192.168.1.%d", i));
        }

        ConsistentHash<String> consistentHash = new ConsistentHash<String>(200, nodes);

        System.out.println("hash circle size: " + consistentHash.getSize());


        HashMap<String, Integer> statisticMap = new HashMap<>();
        for (long i = 0; i < maxKeyCount; i++) {
            String node = consistentHash.get(prefix + random.nextInt());
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

        PerfUtil.analyze(statisticMap, consistentHash.getSize(), nodes.size());
    }

}