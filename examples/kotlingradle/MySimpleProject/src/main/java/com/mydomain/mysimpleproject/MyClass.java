package com.mydomain.mysimpleproject;

public class MyClass {

    public static String getMessage(Lang language) {
        switch (language) {
            case ENGLISH:
                return "Hello";
            case FRENCH:
                return "Bonjour";
            case GERMAN:
                return "Hallo";
            case SPANISH:
                return "Hola";
            default:
                return "Saluton";
        }

    }
}
