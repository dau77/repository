package com.equator.validate;

public class ValidateGroupHolder {
    private final static ThreadLocal<Object[]> holder = new ThreadLocal<>();

    public static void set(Object[] objects) {
        holder.set(objects);
    }

    public static void remove() {
        holder.remove();
    }

    public static Object[] get() {
        return holder.get();
    }
}
