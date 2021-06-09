package com.banana.volunteer.holder;

/**
 * 临时存储用户身份标识
 */
public class UserStatusHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String get() {
        return holder.get() == null ? "null" : holder.get();
    }

    public static void set(String status) {
        holder.set(status);
    }

    public static void remove() {
        holder.remove();
    }

}
