
package com.rengh.java.study.own.algorithm;

import com.rengh.java.study.utils.BaseUtils;

public class MySort {

    public static void main(String[] args) {
        int[] array = new int[10];
        for (int i = 0; i < array.length; i++) {
            int temp = BaseUtils.getRandom(0, 9);
            array[i] = temp;
        }
        BaseUtils.print(array);

        long start = System.currentTimeMillis();
        int[] delta = {
                5, 1
        };
        shellSort(array, delta);
        long end = System.currentTimeMillis();
        System.out.println("数据长度：" + array.length + ", 耗时：" + (end - start) + " ms");
        BaseUtils.print(array);
    }

    /**
     * 一、插入类排序
     */

    /**
     * 1. 直接插入排序
     * 
     * @param array
     */
    // 时间复杂度：O(n^2)
    // 稳定排序
    public static void insertSort(int[] array) {
        if (null == array || 0 >= array.length) {
            return;
        }
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                int temp = array[i];
                array[i] = array[i - 1];
                int j = i - 2;
                while (j >= 0 && temp < array[j]) {
                    array[j + 1] = array[j];
                    j--;
                }
                array[j + 1] = temp;
            }
        }
    }

    /**
     * 2. 折半插入排序
     *
     * @param array
     */
    // 在直接插入的基础上，对需要进行比较的有序序列，进行折半查找需要插入的位置，减少了比较次数，未减少移动次数
    // 时间复杂度：O(n^2)
    // 稳定排序
    public static void binInsertSort(int[] array) {
        if (null == array || 0 >= array.length) {
            return;
        }
        for (int i = 1; i < array.length; i++) {
            int temp = array[i];
            int hi = i - 1, lo = 0;
            while (lo <= hi) {
                int mid = (lo + hi) / 2;
                if (temp < array[mid]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }
            for (int j = i - 1; j > hi; j--) {
                array[j + 1] = array[j];
            }
            array[hi + 1] = temp;
        }
    }

    /**
     * 3. 希尔排序
     *
     * @param array
     */
    // 在直接插入的基础上，对需要进行比较的有序序列，进行折半查找需要插入的位置，减少了比较次数，未减少移动次数
    // 时间复杂度：O(n^2)
    // 稳定排序
    public static void shellSort(int[] array, int[] delta) {
        if (null == array || 0 >= array.length || null == delta || 0 >= delta.length) {
            return;
        }
        for (int i = 1; i < delta.length; i++) {
            shellSort(array, delta[i]);
        }
    }

    private static void shellSort(int[] array, int delta) {
        for (int i = delta; i < array.length; i++) {
            if (array[i] < array[i - delta]) {
                int temp = array[i];
                int j = i - delta;
                while (j >= 0 && temp < array[j]) {
                    array[j + delta] = array[j];
                    j = j - delta;
                }
                array[j + delta] = temp;
            }
        }
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
