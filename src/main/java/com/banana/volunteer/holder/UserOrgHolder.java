package com.banana.volunteer.holder;

/**
 * 临时存储用户所属组织id
 */
public class UserOrgHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static String get() {
        return holder.get() == null ? "null" : holder.get();
    }

    public static void set(String orgName) {
        holder.set(orgName);
    }

    public static void remove() {
        holder.remove();
    }
}
