package com.mydomain.mymultipleproject.server.main;

import com.mydomain.mymultipleproject.common.MyJavaLibrary;
import com.mydomain.mymultipleproject.common.MyKotlinLibraryKt;

public class Server {

    public static void main(String... args) {
        System.out.println("triple(4) = " + MyJavaLibrary.triple(4));
        System.out.println("quadruple(3) = " + MyKotlinLibraryKt.quadruple(3));
    }
}
