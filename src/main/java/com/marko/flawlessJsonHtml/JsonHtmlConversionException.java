package com.marko.flawlessJsonHtml;

/**
 * Exception thrown when JSON to HTML conversion fails.
 */
public class JsonHtmlConversionException extends Exception {

    private static final long serialVersionUID = 1L;

    public JsonHtmlConversionException(String message) {
        super(message);
    }

    public JsonHtmlConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonHtmlConversionException(Throwable cause) {
        super(cause);
    }
}