package com.marko.flawlessJsonHtml.converter;

import com.marko.flawlessJsonHtml.html.HtmlElementBuilder;
import com.marko.flawlessJsonHtml.html.HtmlUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Converts JSON documents to HTML format.
 * <p>
 * Expected JSON structure:
 * {
 *   "doctype": "html",
 *   "language": "en",
 *   "head": { ... },
 *   "body": { ... }
 * }
 */
public class JsonHtmlConverter {

    private static final Logger LOGGER = Logger.getLogger(JsonHtmlConverter.class.getName());
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_DOCTYPE = "html";

    private final HtmlElementBuilder elementBuilder;

    public JsonHtmlConverter() {
        this.elementBuilder = new HtmlElementBuilder();
    }

    /**
     * Converts a JSON object to HTML string.
     *
     * @param root the JSON object to convert
     * @return HTML string representation
     * @throws JsonHtmlConversionException if conversion fails
     * @throws IllegalArgumentException if root is null
     */
    public String convert(JSONObject root) throws JsonHtmlConversionException {
        if (root == null) {
            throw new IllegalArgumentException("JSON root cannot be null");
        }

        try {
            StringBuilder html = new StringBuilder();

            buildDoctype(root, html);
            buildHtmlOpenTag(root, html);
            buildHead(root, html);
            buildBody(root, html);
            html.append("</html>");

            return html.toString();

        } catch (JSONException e) {
            LOGGER.log(Level.SEVERE, "Failed to convert JSON to HTML", e);
            throw new JsonHtmlConversionException("Failed to convert JSON to HTML: " + e.getMessage(), e);
        }
    }

    private void buildDoctype(JSONObject root, StringBuilder html) throws JSONException {
        String doctype = root.optString("doctype", DEFAULT_DOCTYPE);
        html.append("<!DOCTYPE ").append(doctype).append(">\n");
    }

    private void buildHtmlOpenTag(JSONObject root, StringBuilder html) throws JSONException {
        String language = root.optString("language", DEFAULT_LANGUAGE);
        html.append("<html lang=\"").append(HtmlUtils.escapeAttribute(language)).append("\">\n");
    }

    private void buildHead(JSONObject root, StringBuilder html) throws JSONException {
        if (!root.has("head")) {
            return;
        }

        html.append("\t<head>\n");
        elementBuilder.buildElements(root.getJSONObject("head"), html, 2);
        html.append("\t</head>\n");
    }

    private void buildBody(JSONObject root, StringBuilder html) throws JSONException {
        if (!root.has("body")) {
            return;
        }

        JSONObject body = root.getJSONObject("body");
        String attributes = HtmlUtils.formatAttributes(body.optJSONObject("attributes"));

        html.append("\t<body").append(attributes).append(">\n");
        elementBuilder.buildBodyElements(body, html, 2);
        html.append("\t</body>\n");
    }
}