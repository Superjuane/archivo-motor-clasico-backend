package com.jolivan.archivomotorclasicobackend.Utils;

public class LoggingUtils {

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
}
