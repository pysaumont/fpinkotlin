package com.mydomain.mymultipleproject.common;

public class MyJavaLibrary {

    public static int triple(int value) {
        return value * 3;
    }

    public static void main(String... args) {

      System.out.println(MyFileKt.isEven.invoke(2));
      System.out.println(MyFileKt.isEven.invoke(3));
    }
}
