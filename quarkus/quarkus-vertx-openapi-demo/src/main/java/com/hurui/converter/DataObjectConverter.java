package com.hurui.converter;

import java.time.ZonedDateTime;

public class DataObjectConverter {

    public static String serializeZonedDateTime(ZonedDateTime value) {
        return value.toString();
    }

    public static ZonedDateTime deserializeZonedDateTime(String value) {
        return ZonedDateTime.parse(value);
    }
}
