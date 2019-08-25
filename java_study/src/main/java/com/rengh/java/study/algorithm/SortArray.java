
package com.rengh.java.study.algorithm;

import com.rengh.java.study.algorithm.heap.VectorHeap;

import java.util.Arrays;

public class SortArray {
    /**
     * 八大算法测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        int a = 10, b = 20;
        a = a + b;
        b = a - b;
        a = a - b;
        System.out.println("交换数据：a=" + a + ", b=" + b);

        sortTestCase();

    }

    /**
     * 排序算法测试用例
     */
    // 交换数据：a=20, b=10
    // 1. 冒泡排序
    // 使用时间：17897 ms
    // 2. 选择排序
    // 使用时间：5072 ms
    // 3.1 插入排序
    // 使用时间：1041 ms
    // 3.2 折半插入排序
    // 使用时间：820 ms
    // 4. 归并排序
    // 使用时间：16 ms
    // 5. 希尔排序
    // 使用时间：23 ms

    // 6.2 三项查找排序
    // 使用时间：14 ms
    // 7. 基数排序
    // 使用时间：29 ms
    // 8. 堆排序
    // 使用时间：189 ms
    private static void sortTestCase() {
        int[] array = new int[100000];
        for (int i = 0; i < array.length; i++) {
            int temp = getRandom(0, 100000);
            array[i] = temp;
        }

        final boolean DEBUG = false;
        SortArray sortArray = new SortArray();
        sortArray.setDebug(DEBUG);
        if (DEBUG)
            sortArray.print(array);

        int[] copyedArray;
        long start;
        long use;
        // System.out.println("1. 冒泡排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.bubbleSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("2. 选择排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.selectSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("3.1 插入排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.insertSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("3.2 折半插入排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.binaryInsertSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("4. 归并排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.mergerSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("5. 希尔排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.shellSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");
        //
        // System.out.println("6.1 快速排序");
        // copyedArray = array.clone();
        // start = System.currentTimeMillis();
        // sortArray.quickSort(copyedArray);
        // if (DEBUG)
        // sortArray.print(copyedArray);
        // use = System.currentTimeMillis() - start;
        // System.out.println("使用时间：" + use + " ms");

        System.out.println("6.2 三项查找排序");
        copyedArray = array.clone();
        start = System.currentTimeMillis();
        sortArray.quickSort3(copyedArray);
        if (DEBUG)
            sortArray.print(copyedArray);
        use = System.currentTimeMillis() - start;
        System.out.println("使用时间：" + use + " ms");

        System.out.println("7. 基数排序");
        copyedArray = array.clone();
        start = System.currentTimeMillis();
        sortArray.radixSort(copyedArray, 10, 5);
        if (DEBUG)
            sortArray.print(copyedArray);
        use = System.currentTimeMillis() - start;
        System.out.println("使用时间：" + use + " ms");

        System.out.println("8. 堆排序");
        copyedArray = array.clone();
        start = System.currentTimeMillis();
        sortArray.heapSort(copyedArray);
        if (DEBUG)
            sortArray.print(copyedArray);
        use = System.currentTimeMillis() - start;
        System.out.println("使用时间：" + use + " ms");
    }

    /**
     * 获取一个随机数
     *
     * @param max
     * @return
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 获取一个制定范围的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("最大数必须大于最小数");
        }
        return min + (int) (Math.random() * (max - 1 - min));
    }

    /**
     * 对数组的两个元素换位
     *
     * @param array 目标数组
     * @param i 需要交换的索引i
     * @param j 需要交换的另一个索引j
     * @throws NullPointerException if array is null
     * @throws ArrayIndexOutOfBoundsException if i|j is more than array.length-1
     */
    // 公有的API方法，我们一般要对参数进行安全检查，抛出恰当的异常，并在方法的注释中说明
    public static void swapPublic(int[] array, int i, int j) {
        if (array == null)
            throw new NullPointerException("数组不能是空");
        if (i >= array.length - 1 || j >= array.length - 1)
            throw new ArrayIndexOutOfBoundsException("需要交换的索引应在数组长度范围内");
        if (i == j)
            return;
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }

    // 本类中私有的方法一般用断言进行安全检查即可
    private void swap(int[] array, int i, int j) {
        // 断言失败将抛出AssertionError
        assert array != null;
        assert i < array.length && j < array.length;
        if (i == j)
            return;
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }

    private boolean DEBUG = false;

    /**
     * 是否开启 DEBUG 模式
     * 
     * @param debug
     */
    public void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 打印数组
     *
     * @param data
     */
    private void print(int[] data) {
        System.out.print("array >>>> ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + "\t");
        }
        System.out.println();
    }

    /**
     * 1. 冒泡排序
     * 
     * @param array
     */
    // 比较次数：O(n^2)
    // 交换次数：O(n^2)
    // 依次比较两个相邻的元素，将值大的元素交换至右端。
    public void bubbleSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
        for (int i = 0; i < array.length - 1; i++) {
            // 记录某趟是否发生交换，若为false表示数组已处于有序状态
            for (int j = 0; j < array.length - i - 1; j++) {
                if (DEBUG)
                    System.out.println(i + ": " + array[i] + "==>> " + j + ": " + array[j] + "<-->" + (j + 1) + ": " + array[j + 1]);
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                    if (DEBUG)
                        print(array);
                }
            }
        }
    }

    /**
     * 2. 选择排序
     *
     * @param array
     */
    // 比较次数：O(n^2)
    // 交换次数：O(n)
    // 选择排序比冒泡排序的比较时间一致，但交换时间少得多；
    // 每一趟从待排序的数据元素中选出最小（或最大）的一个元素，顺序放在已排好序的数列的最后
    // 直到全部待排序的数据元素排完，它需要经过n-1趟比较。
    public void selectSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (DEBUG)
                    System.out.println(i + ": " + array[i] + "==>> " + j + ": " + array[j] + "<-->" + (j + 1) + ": " + array[j + 1]);
                if (array[j] < array[minIndex])
                    minIndex = j;
            }
            if (minIndex != i) {
                swap(array, i, minIndex);
                if (DEBUG)
                    print(array);
            }
        }
    }

    /**
     * 3.1 插入排序
     * 
     * @param array
     */
    // 随机数据：O(n^2)
    // 部分有序数据：O(n)
    // 一次复制比一次交换耗费的时间少得多，所以插入排序比冒泡排序快一倍，比选择排序略快
    // 对于完全逆序排列的数据，效率和冒泡排序的效率差不多
    public void insertSort(int[] array) {
        insertSort(array, 0, array.length - 1);
    }

    // 之后的快速排序有用到这个方法，所以独立出三个参数的方法
    public void insertSort(int[] array, int start, int end) {
        if (array == null || start < 0 || end < 1)
            return;
        for (int i = start + 1; i <= end; i++) {
            int temp = array[i];
            if (array[i] < array[i - 1]) {
                int j = i - 1;
                // 将有序部分（i之前的都是有序的）与temp（i处的）比较，将大于temp的依次后移一位（移到了i），
                // while结束后就找到了temp应该插入的位置（>temp的后移了，<temp的没动，那么中间空出的就是temp应该插入的位置）
                while (j >= 0 && array[j] > temp) {
                    if (DEBUG)
                        System.out.println(i + ": " + array[i] + "==>> " + j + ": " + array[j] + "<-->" + (j + 1) + ": " + array[j + 1]);
                    array[j + 1] = array[j];
                    j--;
                }
                array[j + 1] = temp;
                if (DEBUG)
                    print(array);
            }
        }
    }

    /**
     * 3.2 折半插入排序
     * 
     * @param array
     */
    // 又叫二分插入排序, 即寻找插入位置时，用二分法寻找
    public void binaryInsertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                // 缓存i处的元素值
                int tmp = array[i];
                // 记录搜索范围的左边界
                int low = 0;
                // 记录搜索范围的右边界
                int high = i - 1;
                while (low <= high) {
                    // 记录中间位置(二分法比较)
                    int mid = (low + high) / 2;
                    // 比较中间位置数据和i处数据大小，以缩小搜索范围
                    if (array[mid] < tmp) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                // 将low~i处数据整体向后移动1位
                for (int j = i; j > low; j--) {
                    array[j] = array[j - 1];
                }
                array[low] = tmp;
                if (DEBUG)
                    print(array);
            }
        }
    }

    /**
     * 4. 归并排序
     * 
     * @param array
     */
    // 序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。
    // 将两个有序表合并成一个有序表，称为二路归并。
    // 优点：效率较高,时间复杂度为O(NlogN), 而冒泡,插入,选择的时间复杂度都是O(N^2)
    // 缺点：需要在存储器中有一个大小等于被排序数组的中介数组,就是用空间换时间
    // 核心：归并两个有序数组
    public void mergerSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
        int[] tempArr = new int[array.length];
        mergerSort(array, tempArr, 0, array.length - 1);
        if (DEBUG)
            print(array);
    }

    // 为了方便本方法中的递归调用，提供了四个参数，并私有化，上面的方法则是供用户使用的公有方法
    private void mergerSort(int[] array, int[] tempArr, int lowerIndex, int upperIndex) {
        if (DEBUG)
            System.out.println("lowerIndex = " + lowerIndex + ", upperIndex = " + upperIndex);
        if (lowerIndex != upperIndex) {
            int mid = (lowerIndex + upperIndex) / 2;
            mergerSort(array, tempArr, lowerIndex, mid);
            mergerSort(array, tempArr, mid + 1, upperIndex);
            marge(array, tempArr, lowerIndex, mid + 1, upperIndex);
        }
    }

    // 归并两个有序部分
    private void marge(int[] array, int[] tempArr, int lowPtr, int highPtr, int upperIndex) {
        int j = 0;
        int lowerIndex = lowPtr;
        int mid = highPtr - 1;
        int n = upperIndex - lowerIndex + 1;
        if (DEBUG)
            System.out.println("lowPtr = " + lowPtr + ", highPtr = " + highPtr + ",lowerIndex = " + lowerIndex + ", upperIndex = " + upperIndex);

        while (lowPtr <= mid && highPtr <= upperIndex) {
            if (array[lowPtr] < array[highPtr])
                tempArr[j++] = array[lowPtr++];
            else
                tempArr[j++] = array[highPtr++];
        }

        while (lowPtr <= mid)
            tempArr[j++] = array[lowPtr++];

        while (highPtr <= upperIndex)
            tempArr[j++] = array[highPtr++];

        for (j = 0; j < n; j++) {
            array[lowerIndex + j] = tempArr[j];
        }
        if (DEBUG)
            print(array);
    }

    /**
     * 5. 希尔排序
     * 
     * @param array
     */
    // 又叫最小增量排序，基于插入排序，时间复杂度O(N*(logN)2)，效率不算太高,适于中等大小数组，但是非常容易实现，代码既简单又短。
    // 原理：通过加大插入排序中元素之间的间隔，并在这些有间隔的元素中进行插入排序，从而使数据项大跨度地移动，当这些数据项排过一趟序之后，希尔排序算法减小数据项的间隔再进行排序，依次进行下去，进行这些排序时的数据项之间的间隔被称为增量，习惯上用字母h来表示这个增量。
    // Shell排序是不稳定的，它的空间开销是O(1)，时间开销估计在O(N3/2)~O(N7/6)之间
    // Shell原稿中建议初始间距为N/2,但这被证明不是最好的数列,会使时间复杂度降低到O(N*N)
    // 后又衍生出N/2.2的优化，其中的关键点在于间隔数列元素的互质性,这使得每一趟排序更有可能保持前一趟排序已排好的效果
    public void shellSort(int[] array) {
        if (array == null || array.length <= 1)
            return;
        if (DEBUG)
            print(array);
        // 增量h
        int h = (int) (array.length / 2.2);
        while (h > 0) {
            if (DEBUG)
                System.out.println("增量h:" + h);
            for (int i = h; i < array.length; i++) {
                int tmp = array[i];
                int j = i - h;
                while (j >= 0 && array[j] > tmp) {
                    array[j + h] = array[j];
                    j -= h;
                }
                array[j + h] = tmp;
            }
            // 设置新的增量
            h /= 2;
        }
        if (DEBUG)
            print(array);
    }

    /**
     * 6.1 快速排序
     * 
     * @param array
     */
    // 划分：快速排序的根本机制, 把数据分为两组,使所有关键字大于特定值的数据项在一组,小于的在另一组, 如全班学生的考试成绩以及格线60划分。
    // 时间复杂度: O(N*logN)
    // 原理：把一个数组分为两个子数组(划分), 然后递归的调用自身,为每个子数组进行快速排序。
    // 效率: 影响效率的关键点在于枢纽的选择(即上面划分中的关键字,例子中的60分)，应尽量保证两个子数组的大小接近
    // 通常来说关键字可以选择任意一项数据,一般选择头尾arr[0]或arr[arr.length-1]，但是这样做算法的性能是不稳定的，因为待排序的数组可能是有序的，会使时间复杂度降到O(N*N)
    // 理想中的枢纽应为待排序数组的中值数据项, 但是选取中间值比较麻烦,所以一个折中的办法就是'三项数据取中'划分,即数组头,尾,中,三个数据项的中值作为枢纽, 这样既简单又可以避免选择到最大或最小值作为枢纽的情况。
    public void quickSort(int[] array) {
        if (DEBUG)
            print(array);
        quickSort(array, 0, array.length - 1);
        if (DEBUG)
            print(array);
    }

    public void quickSort(int[] array, int start, int end) {
        if (start >= end)
            return;
        // 以起始索引为分界点
        int pivot = array[start];
        int i = start + 1;
        int j = end;
        while (true) {
            while (i <= end && array[i] < pivot) {
                i++;
            }
            while (j > start && array[j] > pivot) {
                j--;
            }
            if (i < j) {
                swap(array, i, j);
            } else {
                break;
            }
        }
        // 交换 j和分界点的值
        swap(array, start, j);
        if (DEBUG)
            print(array);
        // 递归左子序列
        quickSort(array, start, j - 1);
        // 递归右子序列
        quickSort(array, j + 1, end);
    }

    /**
     * 6.2 三项查找快速排序
     *
     * @param array
     */
    // 三项数据取中实现快速排序
    public void quickSort3(int[] array) {
        if (DEBUG)
            print(array);
        quickSort3(array, 0, array.length - 1);
        if (DEBUG)
            print(array);
    }

    private void quickSort3(int[] array, int start, int end) {
        int size = end - start + 1;
        // if (size <= 3)
        // manualSort(array, start, end);
        // 对于小划分的处理,上面是限制为3, 手动比较, 下面的没有限制, 使用插入排序,
        // 下面的更方便试用出不同的切割点, 以找到更好的执行效率,这一点很有意义
        if (size < 10)
            insertSort(array, start, end);
        else {
            // 3项取中
            int median = medianOf3(array, start, end);
            // 划分
            int pivotIndex = partitionIt(array, start, end, median);
            // 递归左右子列
            quickSort3(array, start, pivotIndex - 1);
            quickSort3(array, pivotIndex + 1, end);
        }

    }

    // 划分
    private int partitionIt(int[] array, int start, int end, int pivot) {
        int leftPtr = start;
        int rightPtr = end - 1;
        while (true) {
            while (array[++leftPtr] < pivot)
                ;
            while (array[--rightPtr] > pivot)
                ;
            if (leftPtr >= rightPtr)
                break;
            else
                swap(array, leftPtr, rightPtr);
        }
        swap(array, leftPtr, end - 1);
        return leftPtr;
    }

    // 三项数据取中
    private int medianOf3(int[] array, int start, int end) {
        int center = (start + end) / 2;

        if (array[start] > array[center])
            swap(array, start, center);

        if (array[start] > array[end])
            swap(array, start, end);

        if (array[center] > array[end])
            swap(array, center, end);

        swap(array, center, end - 1);// put pivot on right

        return array[end - 1];
    }

    // 当数组/子数组长度<=3时,进行手动排序
    private void manualSort(int[] array, int start, int end) {
        int size = end - start + 1;
        if (size <= 1)
            return;
        if (size == 2) {
            if (array[start] > array[end])
                swap(array, start, end);
            return;
        }
        if (size == 3) {
            if (array[start] > array[end - 1])
                swap(array, start, end - 1);
            if (array[start] > array[end])
                swap(array, start, end);
            if (array[end - 1] > array[end])
                swap(array, end - 1, end);
        }
    }

    /**
     * 7. 基数排序
     * 
     * @param data
     * @param radix
     * @param d
     */
    // 基数:一个数字系统的基,10是十进制系统的基数，2是二进制系统的基数
    // 把数值拆分2位数字位,对每一位进行排序
    // 缺点:以空间换时间
    public void radixSort(int[] data, int radix, int d) {
        // 缓存数组
        int[] tmp = new int[data.length];
        // buckets用于记录待排序元素的信息
        // buckets数组定义了max-min个桶
        int[] buckets = new int[radix];
        for (int i = 0, rate = 1; i < d; i++) {
            // 重置count数组，开始统计下一个关键字
            Arrays.fill(buckets, 0);
            // 将data中的元素完全复制到tmp数组中
            System.arraycopy(data, 0, tmp, 0, data.length);

            // 计算每个待排序数据的子关键字
            for (int j = 0; j < data.length; j++) {
                int subKey = (tmp[j] / rate) % radix;
                buckets[subKey]++;
            }

            for (int j = 1; j < radix; j++) {
                buckets[j] = buckets[j] + buckets[j - 1];
            }

            // 按子关键字对指定的数据进行排序
            for (int m = data.length - 1; m >= 0; m--) {
                int subKey = (tmp[m] / rate) % radix;
                data[--buckets[subKey]] = tmp[m];
            }
            rate *= radix;
        }
    }

    /**
     * 8. 堆排序
     * 
     * @param array
     */
    // 其中VectorHeap是一个自己写到堆的实现类，关于堆到后面介绍到堆时再详细介绍，下面先给出其具体实现类的代码
    public void heapSort(int[] array) {
        if (DEBUG)
            print(array);
        VectorHeap<Integer, Integer> vectorHeap = new VectorHeap<>();
        for (int i = 0; i < array.length; i++)
            vectorHeap.insert(array[i], null);
        for (int i = 0; i < array.length; i++)
            array[array.length - 1 - i] = vectorHeap.remove().key;
        if (DEBUG)
            print(array);
    }

}
