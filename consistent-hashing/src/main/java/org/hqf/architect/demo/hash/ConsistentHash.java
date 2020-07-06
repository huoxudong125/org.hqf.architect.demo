package org.hqf.architect.demo.hash;

import org.hqf.architect.demo.hash.stat.HashStrategy;

import java.util.*;

public class ConsistentHash<T> {

    private final HashStrategy hashStrategy;

    // 实际节点的虚拟副本因子,实际节点个数 * numberOfVirtual =虚拟节点个数
    private final int nodeVirtualFactor;

    // 存储虚拟节点的hash值到真实节点的映射
    private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

    public ConsistentHash(int nodeVirtualFactor,
                          Collection<T> nodes,
                          HashStrategy hashStrategy){
        this.hashStrategy=hashStrategy;
        this.nodeVirtualFactor = nodeVirtualFactor;
        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < nodeVirtualFactor; i++) {
            // 对于一个实际机器节点 node, 对应 nodeVirtualFactor 个虚拟节点
            /*
             * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
             * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
             */
            String nodeStr = node.toString() + i;
            int hashcode = nodeStr.hashCode();
//            System.out.println("hashcode:" + hashcode);
            circle.put(hashcode, node);

        }
    }

    public void remove(T node) {
        for (int i = 0; i < nodeVirtualFactor; i++)
            circle.remove((node.toString() + i).hashCode());
    }

    /*
     * 获得一个最近的顺时针节点,根据给定的key 取Hash
     * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点
     * 再从实际节点中取得 数据
     */
    public T get(Object key) {
        if (circle.isEmpty())
            return null;
//        int hash = key.hashCode();// node 用String来表示,获得node在哈希环中的hashCode
        int hash;
        if(hashStrategy==null){
            hash = key.hashCode();// node 用String来表示,获得node在哈希环中的hashCode
        }else {
            hash = hashStrategy.getHashCode(key);
        }
        System.out.println("hashcode----->:" + hash);
        if (!circle.containsKey(hash)) {
            //数据映射在两台虚拟机器所在环之间,就需要按顺时针方向寻找机器
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public int getSize() {
        return circle.size();
    }

    public int getNodeVirtualFactor() {
        return nodeVirtualFactor;
    }

    public Map<Integer, T> getMap() {
        return circle;
    }




}

