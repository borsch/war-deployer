package com.charlesworthgroup.deployer;

public class Log {

    public static void info(String msg, Object... args) {
        print(String.format(
                "[INFO]\t\t" + msg, args
        ));
    }

    public static void error(String msg, Object... args) {
        print(String.format(
                "[ERROR]\t\t" + msg, args
        ));
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

}
