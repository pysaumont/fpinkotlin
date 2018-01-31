package com.fpinkotlin.workingwithlaziness.listing01;

import java.util.stream.Stream;

public class TestStream {
    public static void main(String... args) {
        Stream<Integer> stream = Stream.iterate(0, i -> i + 1);
        stream.findFirst().ifPresent(System.out::println);
        stream.findFirst().ifPresent(System.out::println);
    }
}
