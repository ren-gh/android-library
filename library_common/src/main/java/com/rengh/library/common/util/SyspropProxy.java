
package com.rengh.library.common.util;

import java.lang.reflect.Method;

import android.content.Context;

/**
 * Created by rengh on 17-5-29.
 */
public class SyspropProxy {

    /**
     * 根据给定Key获取值.
     *
     * @return 如果不存在该key则返回空字符串
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public static String get(Context context, String key) throws IllegalArgumentException {
        String ret = "";
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method get = SystemProperties.getMethod("get", String.class);

            ret = (String) get.invoke(SystemProperties, key);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = "";
        }

        return ret;
    }

    /**
     * 根据Key获取值.
     *
     * @return 如果key不存在, 并且如果def不为空则返回def否则返回空字符串
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public static String get(Context context, String key, String def)
            throws IllegalArgumentException {
        String ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method get = SystemProperties.getMethod("get", String.class, String.class);

            ret = (String) get.invoke(SystemProperties, key, def);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    /**
     * 根据给定的key返回int类型值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个int类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public static Integer getInt(Context context, String key, int def)
            throws IllegalArgumentException {
        Integer ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method getInt = SystemProperties.getMethod("getInt", String.class, int.class);

            ret = (Integer) getInt.invoke(SystemProperties, key, def);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    /**
     * 根据给定的key返回long类型值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个long类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public static Long getLong(Context context, String key, long def)
            throws IllegalArgumentException {
        Long ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method getLong = SystemProperties.getMethod("getLong", String.class, long.class);

            ret = (Long) getLong.invoke(SystemProperties, key, def);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    /**
     * 根据给定的key返回boolean类型值. 如果值为 'n', 'no', '0', 'false' or 'off' 返回false. 如果值为'y', 'yes', '1', 'true' or 'on' 返回true. 如果key不存在, 或者是其它的值, 则返回默认值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个boolean类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public static Boolean getBoolean(Context context, String key, boolean def)
            throws IllegalArgumentException {
        Boolean ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method getBoolean = SystemProperties.getMethod("getBoolean", String.class, boolean.class);

            ret = (Boolean) getBoolean.invoke(SystemProperties, key, def);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
        }

        return ret;
    }

    /**
     * 根据给定的key和值设置属性, 该方法需要特定的权限才能操作.
     *
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     * @throws IllegalArgumentException 如果value超过92个字符则抛出该异常
     */
    public static void set(Context context, String key, String val) throws IllegalArgumentException {
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");

            @SuppressWarnings("unchecked")
            Method set = SystemProperties.getMethod("set", String.class, String.class);

            set.invoke(SystemProperties, key, val);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
        }
    }
}

