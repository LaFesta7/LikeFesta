package com.sparta.lafesta.common.entity;

public class StringFormatter {
    public static String format(String requestString) {
        requestString = requestString.replaceAll("&", "&amp;");
        requestString = requestString.replaceAll("<", "&lt;");
        requestString = requestString.replaceAll(">", "&gt;");
        requestString = requestString.replaceAll("￦", "&quot;");
        requestString = requestString.replaceAll("'", "&#x27;");
        requestString = requestString.replaceAll("/", "&#x2F;");
        requestString = requestString.replaceAll("\\(", "&#x28;");
        requestString = requestString.replaceAll("\\)", "&#x29;");
        return requestString;
    }
}
