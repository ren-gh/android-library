
package com.rengh.java.study.algorithm2;

public class MySort {
    public static void main(String[] args) {
        int[] array = new int[200000];
        for (int i = 0; i < array.length; i++) {
            int temp = getRandom(0, 1000000);
            array[i] = temp;
        }
        print(array);

        long start = System.currentTimeMillis();
        selectSort(array, false);
        long end = System.currentTimeMillis();
        System.out.println("选择排序，数据长度：" + array.length + ", 耗时：" + (end - start) + " ms");
        print(array);
    }

    /**
     * 获取一个随机数
     *
     * @param max 包含
     * @return
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 获取一个指定范围的随机数
     *
     * @param min 包含
     * @param max 包含
     * @return
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("最大数必须大于最小数");
        }
        return min + (int) (Math.random() * (max - min + 1));
    }

    /**
     * 打印数组
     * 
     * @param array
     */
    public static void print(int[] array) {
        if (null == array) {
            System.out.println("数组为 null");
            return;
        }
        if (0 == array.length) {
            System.out.println("数组长度为 0");
            return;
        }
        int n = array.length;
        if (20 < array.length) {
            System.out.println("数组元素太多，只打印前二十个：");
            n = 20;
        }
        System.out.print("{");
        for (int i = 0; i < n; i++) {
            System.out.print(array[i]);
            if (i != n - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    /**
     * 选择排序
     * 
     * @param array
     */
    public static void selectSort(int[] array) {
        selectSort(array, false);
    }

    public static void selectSort(int[] array, boolean big2small) {
        if (null == array) {
            throw new NullPointerException("数组不能为 null");
        }
        if (0 == array.length) {
            return;
        }
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minOrMax = i;
            for (int j = i + 1; j < n; j++) {
                if (big2small) {
                    if (array[minOrMax] < array[j]) {
                        minOrMax = j;
                    }
                } else {
                    if (array[minOrMax] > array[j]) {
                        minOrMax = j;
                    }
                }
            }
            if (i != minOrMax) {
                int temp = array[i];
                array[i] = array[minOrMax];
                array[minOrMax] = temp;
            }
        }
    }
}
