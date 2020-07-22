package org.hqf.architect.demo.perf;

import org.apache.commons.lang.time.StopWatch;

import javax.lang.model.SourceVersion;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class RestTester {


    public static void main(String[] args) throws InterruptedException {

        if (args.length < 3) {
            System.out.println("params is: url,concurrentNum, totolReuqest,eg. www.baiud.com,10,100");
        }

        String url = args[0];
        int concurrentNum = Integer.parseInt(args[1]);
        int RequestTestCount = Integer.parseInt(args[2]);

        List<Long> durationSet = Collections.synchronizedList(new ArrayList<>());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < RequestTestCount; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(concurrentNum);
            for (int j = 0; j < concurrentNum; j++) {
                Thread thread = new Thread(() -> {
                    long workTime = doWork(url);
                    synchronized (RestTester.class) {
                        durationSet.add(workTime);
                    }
                    countDownLatch.countDown();
                });
                thread.start();
            }
            countDownLatch.await();
        }


        stopWatch.stop();
        System.out.println(String.format("处理完整个测试的结束时间 %d ms,%d", stopWatch.getTime(), durationSet.size()));

        List<Long> DurationSetP95 = new ArrayList<>();
        Iterator<Long> iterator = durationSet.stream().sorted().iterator();
        for (int i = 0; i < Math.ceil(durationSet.size() * 0.95); i++) {
            DurationSetP95.add(iterator.next());
        }
        DoubleSummaryStatistics doubleSummaryStatistics = DurationSetP95.stream().mapToDouble(t -> t.doubleValue()).summaryStatistics();

        System.out.println("95% 的输出 avg = " + doubleSummaryStatistics);
    }

    public static long doWork(String url) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            HttpClientUtil.doGet2(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        long time = stopWatch.getTime();
        System.out.println(String.format("%s 处理行数据,时间 %d ms", Thread.currentThread().getName(), time));
        return time;
    }
}
