
package com.rengh.java.study.own.algorithm;

import com.rengh.java.study.utils.BaseUtils;

public class MySort {
    /**
     * 树形选择排序、堆排序、计数排序
     * 
     * @param args
     */

    public static void main(String[] args) {
        int[] array = new int[8];
        for (int i = 0; i < array.length; i++) {
            int temp = BaseUtils.getRandom(0, 9);
            array[i] = temp;
        }
        BaseUtils.print(array);

        System.out.println("开始排序：");

        long start = System.currentTimeMillis();
        // int[] delta = {
        // 5, 1
        // };
        // shellSort(array, delta);
        heapSort(array);
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
     * 二、交换类排序：冒泡排序、快速排序
     */

    /**
     * 1. 冒泡排序
     * 
     * @param array
     */
    // 从第一个与第二个开始，依次比对两个元素，逆序则交换，共比较 n -1 趟；
    // 时间复杂度：O(n^2)
    // 稳定排序
    public static void bubbleSort(int[] array) {
        if (null == array || 0 == array.length) {
            return;
        }
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < array.length - i; j++) {
                if (array[j] > array[j + 1]) {
                    array[j] = array[j] + array[j + 1];
                    array[j + 1] = array[j] - array[j + 1];
                    array[j] = array[j] - array[j + 1];
                }
            }
        }
    }

    /**
     * 2. 快速排序
     * 
     * @param arr
     */
    // 最好情况：O(nlogn)；最坏情况：O(n²)
    // 采用三元取中法，取一个不大不小的数作为基准数，规避最坏情况；
    public static void quickSort(int[] arr) {
        if (null == arr || 1 >= arr.length) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int pa = partition(arr, left, right);
            quickSort(arr, left, pa - 1);
            quickSort(arr, pa + 1, right);
        }
    }

    private static int partition(int[] arr, int left, int right) {
        // 采用三数中值分割法
        int mid = left + (right - left) / 2;
        // 保证左端较小
        if (arr[left] > arr[right])
            BaseUtils.swap(arr, left, right);
        // 保证中间较小
        if (arr[mid] > arr[right])
            BaseUtils.swap(arr, mid, right);
        // 保证中间最小，左右最大
        if (arr[mid] > arr[left])
            BaseUtils.swap(arr, left, mid);

        int temp = arr[left];
        while (right > left) {
            // 先判断基准数和后面的数依次比较
            while (temp <= arr[right] && left < right) {
                --right;
            }
            // 当基准数大于了 arr[right]，则填坑
            if (left < right) {
                arr[left] = arr[right];
                ++left;
            }
            // 现在是 arr[right] 需要填坑了
            while (temp >= arr[left] && left < right) {
                ++left;
            }
            if (left < right) {
                arr[right] = arr[left];
                --right;
            }
        }
        arr[left] = temp;
        return left;
    }

    /**
     * 三、选择类排序
     */

    /**
     * 1. 选择排序
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

    /**
     * 2. 树形选择排序
     */

    /**
     * 3. 堆排序
     * 
     * @param list
     */
    // 在任何情况下，堆排序的时间复杂度均为 O(n log n)，这是堆排序最大的优点
    public static void heapSort(int[] list) {
        // 构造初始堆,从第一个非叶子节点开始调整,左右孩子节点中较大的交换到父节点中
        for (int i = (list.length) / 2 - 1; i >= 0; i--) {
            heapAdjust(list, list.length, i);
        }
        // 排序，将最大的节点放在堆尾，然后从根节点重新调整
        for (int i = list.length - 1; i >= 1; i--) {
            int temp = list[0];
            list[0] = list[i];
            list[i] = temp;
            heapAdjust(list, i, 0);
        }
    }

    private static void heapAdjust(int[] list, int len, int i) {
        int k = i, temp = list[i], index = 2 * k + 1;
        while (index < len) {
            if (index + 1 < len) {
                if (list[index] < list[index + 1]) {
                    index = index + 1;
                }
            }
            if (list[index] > temp) {
                list[k] = list[index];
                k = index;
                index = 2 * k + 1;
            } else {
                break;
            }
        }
        list[k] = temp;
    }

    /**
     * 四、归并排序
     */

    /**
     * 归并排序
     *
     * @param r
     */
    public static void mergeSort(int[] r) {
        if (null == r || 1 >= r.length) {
            return;
        }
        mergeSort(r, 0, r.length - 1);
    }

    public static void mergeSort(int[] r, int low, int high) {
        if (low < high) {
            mergeSort(r, low, (high + low) / 2);
            mergeSort(r, (high + low) / 2 + 1, high);
            merge(r, low, (high + low) / 2, high);
        }
    }

    private static void merge(int[] a, int p, int q, int r) {
        int[] b = new int[r - p + 1];
        int s = p;
        int t = q + 1;
        int k = 0;
        while (s <= q && t <= r) {
            if (a[s] < a[t]) {
                b[k++] = a[s++];
            } else {
                b[k++] = a[t++];
            }
        }
        while (s <= q) {
            b[k++] = a[s++];
        }
        while (t <= r) {
            b[k++] = a[t++];
        }
        for (int i = 0; i < b.length; i++) {
            a[p + i] = b[i];
        }
    }

    /**
     * 非比较排序
     */

    /**
     * 1. 计数排序
     */

    /**
     * 2. 基数排序
     * 
     * @param arr
     */
    public static void radixSort(int[] arr) {
        // 存数组中最大的数，目的获取其位数
        int max = Integer.MIN_VALUE;
        for (int x : arr) {
            if (x > max) {
                max = x;
            }
        }
        // 最大值长度
        int maxLength = (max + "").length();
        // 创建一个二维数组存储临时数据
        int[][] temp = new int[10][arr.length];
        // 创建一个数组，用来记录temp内层数组存储元素的个数
        int[] count = new int[10];
        // 将数据放入二维数组中
        for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
            for (int j = 0; j < arr.length; j++) {
                int number = arr[j] / n % 10;
                // 将number放入指定数组的指定位置
                temp[number][count[number]] = arr[j];
                // 将count数组中记录元素个数的元素+1
                count[number]++;
            }
            // 从二维数组中取数据
            int index = 0;
            for (int x = 0; x < count.length; x++) {
                if (count[x] != 0) {
                    for (int y = 0; y < count[x]; y++) {
                        arr[index] = temp[x][y];
                        index++;
                    }
                    count[x] = 0;
                }
            }
        }
    }
}
