package com.jolivan.pruebaJava;

import com.fasterxml.jackson.datatype.jsr310.ser.ZoneIdSerializer;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class pruebaZonedDateTimeFormat {

    public static void main(String... args) {

        formatprueba("2021-08-01T00:00:01Z");

    }

    private static void formatprueba(String s) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(s);
        System.out.println(zonedDateTime);
    }
}
