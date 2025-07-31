package com.marko.flawlessJsonHtml;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Utility class for HTML-related operations.
 */
public final class HtmlUtils {

    private HtmlUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates indentation string with tabs.
     *
     * @param level indentation level
     * @return indentation string
     */
    public static String createIndent(int level) {
        if (level <= 0) return "";
        return "\t".repeat(level);
    }

    /**
     * Formats JSON object as HTML attributes.
     *
     * @param attrs JSON object containing attributes
     * @return formatted attribute string
     * @throws JSONException if JSON processing fails
     */
    public static String formatAttributes(JSONObject attrs) throws JSONException {
        if (attrs == null || attrs.length() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        Iterator<String> keys = attrs.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = attrs.get(key);

            if (value instanceof JSONObject && "style".equals(key)) {
                String styleValue = formatStyleAttribute((JSONObject) value);
                if (!styleValue.isEmpty()) {
                    result.append(" ").append(key).append("=\"").append(styleValue).append("\"");
                }
            } else if (value != null) {
                result.append(" ").append(key).append("=\"")
                        .append(escapeAttribute(value.toString())).append("\"");
            }
        }

        return result.toString();
    }

    /**
     * Formats a style object as CSS string.
     */
    private static String formatStyleAttribute(JSONObject styleObj) throws JSONException {
        StringBuilder style = new StringBuilder();
        Iterator<String> styleKeys = styleObj.keys();

        while (styleKeys.hasNext()) {
            String styleKey = styleKeys.next();
            String styleValue = styleObj.getString(styleKey);

            if (style.length() > 0) {
                style.append(" ");
            }
            style.append(styleKey).append(": ").append(styleValue).append(";");
        }

        return style.toString();
    }

    /**
     * Escapes HTML content (text between tags).
     *
     * @param content content to escape
     * @return escaped content
     */
    public static String escapeContent(String content) {
        if (content == null) return "";

        return content.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    /**
     * Escapes HTML attribute values.
     *
     * @param attribute attribute value to escape
     * @return escaped attribute value
     */
    public static String escapeAttribute(String attribute) {
        if (attribute == null) return "";

        return attribute.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}