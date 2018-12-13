
package com.rengh.lib.java;

import com.rengh.lib.java.common.FileUtils;

import java.io.File;

public class MainClass {
    public static void main(String[] args) {
        File file = new File("mac_result.txt");
        String path = file.getAbsolutePath();
        System.out.println("路径：" + path);
        System.out.println("存在：" + file.exists());
        for (int i = 0; i < 10; i++) {
            String content = FileUtils.getContent("mac_result.txt", i * 50000, 50000);
            FileUtils.write("mac_" + i + ".csv", content, false);
        }
    }
}
