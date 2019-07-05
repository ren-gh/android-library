
package com.r.library.common.test;

import com.r.library.common.util.ThreadUtils;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VolatileTest {

    public static void main(String[] args) {
        BlockQueueTest blockQueueTest = new BlockQueueTest(20);
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    Runnable runnable = (Runnable) blockQueueTest.take();
                    if (null != runnable) {
                        runnable.run();
                    } else {
                        i++;
                        System.out.println("take() count=" + i + ", runnable is null.");
                    }
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    final int number = blockQueueTest.addNumber();
                    blockQueueTest.put(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Thread 1, runnable: " + number);
                        }
                    });
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    final int number = blockQueueTest.addNumber();
                    blockQueueTest.put(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Thread 2, runnable: " + number);
                        }
                    });
                }
            }
        }.start();
    }

    private synchronized void run(Runnable runnable){

    }

    // 先进先出
    public static class BlockQueueTest {
        private int number;
        private LinkedList<Object> inc;
        private ReentrantLock lock;
        private Condition takeCondition, putCondition;
        private int index;
        private int max;

        public BlockQueueTest() {
            this(Integer.MAX_VALUE);
        }

        public BlockQueueTest(int count) {
            if (count < 1) {
                throw new IllegalArgumentException();
            }
            max = count;
            inc = new LinkedList<>();
            lock = new ReentrantLock();
            takeCondition = lock.newCondition();
            putCondition = lock.newCondition();
        }

        public synchronized int addNumber() {
            number++;
            return number;
        }

        public void put(Runnable runnable) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                while (index == max) {
                    System.out.println("PUT 达到最大值，等待...");
                    putCondition.await();
                }
                inc.add(runnable);
                System.out.println("已加入数据，" + index);
                index++;
                takeCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public Object take() {
            final ReentrantLock lock = this.lock;
            lock.lock();
            Object object = null;
            try {
                while (index == 0) {
                    System.out.println("TAKE 无数据，等待...");
                    takeCondition.await();
                }
                index--;
                object = inc.removeFirst();
                System.out.println("已消费数据，" + index);
                putCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                return object;
            }
        }
    }
}
