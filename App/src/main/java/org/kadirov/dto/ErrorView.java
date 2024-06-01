package org.kadirov.dto;

public record ErrorView(
        int statusCode,
        String message
) {

    public static ErrorView of(int statusCode, String message){
        return new ErrorView(statusCode, message);
    }
}
