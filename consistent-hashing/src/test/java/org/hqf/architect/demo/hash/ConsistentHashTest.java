package org.hqf.architect.demo.hash;

import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ConsistentHashTest {

    @Test
    public void test(){
        Set<String> nodes = new HashSet<String>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");

        ConsistentHash<String> consistentHash = new ConsistentHash<String>(2, nodes);
        consistentHash.add("D");

        System.out.println("hash circle size: " + consistentHash.getSize());
        System.out.println("location of each node are follows: ");
        consistentHash.testBalance();

        String node =consistentHash.get("apple");
        System.out.println("node----------->:"+node);

        System.out.println(" ============================ ");
        for (Map.Entry<Integer, String> integerStringEntry : consistentHash.getMap().entrySet()) {
            System.out.println("integerStringEntry = " + integerStringEntry);
        }
//        PerfUtil.analyze(,consistentHash.getSize(),nodes.size());
    }

}