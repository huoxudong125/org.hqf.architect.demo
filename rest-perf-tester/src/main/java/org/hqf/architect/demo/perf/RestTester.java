package org.hqf.architect.demo.perf;

import java.util.concurrent.CountDownLatch;

public class RestTester {

    static CountDownLatch countDownLatch = new CountDownLatch(100);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                doWork();
                countDownLatch.countDown();
            });
            thread.start();

        }
        countDownLatch.await();
        System.out.println("处理完整个测试的结束时间" + System.currentTimeMillis());
    }

    public static void doWork() {
        System.out.println("处理行数据,时间" + System.currentTimeMillis());
    }
}
