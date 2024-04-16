package com.jolivan.archivomotorclasicobackend.Utils;

public class LoggingUtils {
    public static final String RED = "\u001B[31m";


    public static void Log(String message) {
        Log(message, false, false);
    }
    public static void Log(String message, boolean timeStamp, boolean extraInfo ) {
        System.out.print("-----");
        if (timeStamp) {
            System.out.printf("[%s] ", java.time.LocalTime.now());
        }
        System.out.print("-----");
        System.out.println(message);
    }

    public static void LogError(String message) {
        System.out.print(RED + "-----" + RED);
        System.out.printf(RED + "[%s]"+RED, java.time.LocalTime.now());
        System.out.print(RED+"-----!! Error: " + message + RED+"\n");
    }
}
