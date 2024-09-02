package com.duva.duva.config;

public class TestLibraryLoad {
    static {
        System.loadLibrary("jniortools");
    }

    public static void main(String[] args) {
        System.out.println("Library loaded successfully!");
    }
}
