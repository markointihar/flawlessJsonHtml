package com.marko.flawlessJsonHtml;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.marko.flawlessJsonHtml.JsonHtmlConverter;

/**
 * main.java.com.marko.flawlessJsonHtml.Main application class for JSON to HTML conversion.
 *
 * Usage: java main.java.com.marko.flawlessJsonHtml.Main [input.json] [output.html]
 * If no arguments provided, defaults to "input.json" and "output.html"
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int SUCCESS_EXIT_CODE = 0;
    private static final int ERROR_EXIT_CODE = 1;

    public static void main(String[] args) {
        try {
            JsonHtmlConverterApp app = new JsonHtmlConverterApp();
            app.run(args);
            System.exit(SUCCESS_EXIT_CODE);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Application failed", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(ERROR_EXIT_CODE);
        }
    }
}
