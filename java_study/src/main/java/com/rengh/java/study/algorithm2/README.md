# 排序

## 前言、辅助方法

#### 获取随机数
```
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
     * 获取一个制定范围的随机数
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
```

#### 打印数组
```
    /**
     * 打印数组
     *
     * @param array
     */
    public static void print(int[] array) {
        if (null == array) {
            System.out.println("null");
            return;
        }
        if (0 == array.length) {
            System.out.println("length is 0");
            return;
        }
        System.out.print("{");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i != array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
```

## 一、选择排序

#### 原理
- 从位置 0 开始，跟位置 1 一直到最后的元素进行比较，找到最小元素的位置，将最小元素和 0 进行交换；
- 从位置 1 开始，跟位置 2 一直到最后的元素进行比较，找到最小元素的位置，将最小元素和 1 进行交换；
- …………
- …………
- …………
- 从位置 n - 2 开始，跟位置 n - 1 元素进行比较，如果 n - 1 小于 n - 2，将 n - 1 和 n - 2 进行交换；

#### 代码
```
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
            throw new NullPointerException("Array must not be null.");
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
```

## 二、
