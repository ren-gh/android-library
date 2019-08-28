# 数据结构与算法（JAVA 语言版）笔记

# 一、 JAVA 基础
## 位运算
&（与）、|（或）、^（异或）、~（非）、>>（右移）、<<（左移、>>>（高位填充 0 的右移））

## 三元运算符
?:

## 变量交换
```
int a = 10, b = 20;
a = a + b;
b = a - b;
a = a - b;
System.out.println("交换数据：a=" + a + ", b=" + b);
```

## 数组
```
int[] K = {
    1, 1, 1
};
// F 和 K 指向同一数组
int[] F = K;
// 拷贝数组方式一：
// int[] F = K.clone();
// 拷贝数组方式二：
// int[] F = new int[K.length];
// System.arraycopy(K, 0, F, 0, K.length);
for (int i = 0; i < F.length; i++) {
    F[i]++;
}
for (int tmp : K) {
    System.out.println(tmp);
}
```

## 对象
- 拷贝：实现 Cloneable 接口并重写 clone 方法
- 比较：重写继承自 Object 类的 equals 方法


# 二、数据结构与算法基础





