package com.marko.flawlessJsonHtml.converter;

import java.io.Serial;

/**
 * Exception thrown when JSON to HTML conversion fails.
 */
public class JsonHtmlConversionException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public JsonHtmlConversionException(String message) {
        super(message);
    }

    public JsonHtmlConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}