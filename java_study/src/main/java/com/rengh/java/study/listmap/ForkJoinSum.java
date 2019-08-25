
package com.rengh.java.study.listmap;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSum extends RecursiveTask<Long> {

    private static final long serialVersionUID = 6011408981548802596L;

    private long start;
    private long end;
    // 临界值
    private final long THRESHHOLD = 10000L;

    public ForkJoinSum() {

    }

    public ForkJoinSum(long start, long end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHHOLD) {
            long sum = 0L;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            long mid = (start + end) / 2;
            ForkJoinSum left = new ForkJoinSum(start, mid);
            left.fork(); // 分支

            ForkJoinSum right = new ForkJoinSum(mid + 1, end);
            right.fork(); // 分支

            return left.join() + right.join(); // 合并
        }
    }

    public static void main(String[] args) {
        Instant start = Instant.now(); // 100000000L 1000000000L 50000000000L
        ForkJoinTask<Long> forkJoinTask = new ForkJoinSum(0L, 50000000000L);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Long t = forkJoinPool.invoke(forkJoinTask);
        System.out.println(t);
        Instant end = Instant.now();
        System.out.println("Stream-五百亿求和耗费的时间为： " + Duration.between(start, end).toMillis());

        testFor();
    }

    private static void testFor() {
        Instant start = Instant.now();
        long sum = 0;
        for (long i = 0; i <= 50000000000L; i++) {
            sum += i;
        }
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println("ForEach-五百亿求和花费的时间为: " + Duration.between(start, end).toMillis());
    }
}
