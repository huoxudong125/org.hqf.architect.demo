package org.hqf.architect.demo.hash;

import org.hqf.architect.demo.hash.stat.HashStrategy;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHash<T> {

    private final HashStrategy hashStrategy;

    /**
     * 实际节点的虚拟副本因子,实际节点个数 * numberOfVirtual =虚拟节点个数
     */
    private final int nodeVirtualFactor;

    /**
     * 存储虚拟节点的hash值到真实节点的映射
     */
    private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

    public ConsistentHash(int nodeVirtualFactor,
                          Collection<T> nodes,
                          HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
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
            String nodeStr = node.toString() + "#VN#" + i;
//            Long hashcode = new Long(nodeStr.hashCode());
            Long hashcode = hash(nodeStr, 0);
//            System.out.println("hashcode:" + hashcode);
            circle.put(hashcode, node);

        }
    }

    public void remove(T node) {
        for (int i = 0; i < nodeVirtualFactor; i++) {
            circle.remove((node.toString() + i).hashCode());
        }
    }

    /**
     * 获得一个最近的顺时针节点,根据给定的key 取Hash
     * 然后再取得顺时针方向上最近的一个虚拟节点对应的实际节点
     * 再从实际节点中取得 数据
     */
    public T getNode(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
//        int hash = key.hashCode();// node 用String来表示,获得node在哈希环中的hashCode
        Long hash;
        if (hashStrategy == null) {
            // node 用String来表示,获得node在哈希环中的hashCode
            hash = new Long(key.hashCode());
        } else {
            hash = hashStrategy.getHashCode(key);
        }
//        System.out.println("hashcode----->:" + hash);
        if (!circle.containsKey(hash)) {
            //得到大于该Hash值的所有Map
            //数据映射在两台虚拟机器所在环之间,就需要按顺时针方向寻找机器
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
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

    public Map<Long, T> getMap() {
        return circle;
    }


    private Long hash(String nodeName, int number) {
        byte[] digest = md5(nodeName);
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public byte[] md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(str.getBytes("UTF-8"));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

