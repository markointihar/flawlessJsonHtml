package com.marko.flawlessJsonHtml.html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;


public class HtmlElementBuilder {

    private static final Logger LOGGER = Logger.getLogger(HtmlElementBuilder.class.getName());


    private enum SelfClosingTag {
        META("meta"), LINK("link"), IMG("img"), BR("br"),
        HR("hr"), INPUT("input"), SOURCE("source"),
        TRACK("track"), WBR("wbr");

        private final String tagName;
        private static final Set<String> TAG_NAMES = EnumSet.allOf(SelfClosingTag.class)
                .stream()
                .map(tag -> tag.tagName)
                .collect(java.util.stream.Collectors.toSet());

        SelfClosingTag(String tagName) {
            this.tagName = tagName;
        }

        public static boolean contains(String tagName) {
            return TAG_NAMES.contains(tagName.toLowerCase());
        }
    }

    private static final List<String> BODY_ELEMENT_ORDER = Arrays.asList(
            "header", "nav", "main", "section", "article", "aside", "footer",
            "h1", "h2", "h3", "h4", "h5", "h6",
            "p", "blockquote", "pre", "code",
            "ul", "ol", "li", "dl", "dt", "dd",
            "div", "span", "a", "img", "video", "audio",
            "form", "input", "button", "select", "textarea"
    );

    /**
     * Builds HTML elements from a JSON object.
     */
    public void buildElements(JSONObject json, StringBuilder html, int indentLevel) throws JSONException {
        if (json == null) return;

        for (Iterator<String> it = json.keys(); it.hasNext();) {
            String tagName = it.next();
            Object value = json.get(tagName);
            buildTag(tagName, value, html, indentLevel);
        }
    }

    public void buildBodyElements(JSONObject body, StringBuilder html, int indentLevel) throws JSONException {
        if (body == null) return;

        Set<String> processedKeys = new HashSet<>();

        // Process elements in semantic order
        for (String tagName : BODY_ELEMENT_ORDER) {
            if (body.has(tagName) && isReservedKey(tagName)) {
                buildTag(tagName, body.get(tagName), html, indentLevel);
                processedKeys.add(tagName);
            }
        }

        // Process remaining elements
        for (Iterator<String> it = body.keys(); it.hasNext();) {
            String tagName = it.next();
            if (!processedKeys.contains(tagName) && isReservedKey(tagName)) {
                buildTag(tagName, body.get(tagName), html, indentLevel);
            }
        }
    }


    public void buildTag(String tagName, Object value, StringBuilder html, int indentLevel) throws JSONException {
        if (tagName == null || value == null) return;

        String indent = HtmlUtils.createIndent(indentLevel);

        if (value instanceof String) {
            buildSimpleTag(tagName, (String) value, html, indent);
        } else if (value instanceof JSONObject) {
            buildObjectTag(tagName, (JSONObject) value, html, indent, indentLevel);
        } else if (value instanceof JSONArray) {
            buildArrayTags(tagName, (JSONArray) value, html, indentLevel);
        } else {
            LOGGER.warning("Unsupported value type for tag '" + tagName + "': " + value.getClass());
        }
    }

    private void buildSimpleTag(String tagName, String content, StringBuilder html, String indent) {
        html.append(indent)
                .append("<").append(tagName).append(">")
                .append(HtmlUtils.escapeContent(content))
                .append("</").append(tagName).append(">\n");
    }

    private void buildObjectTag(String tagName, JSONObject obj, StringBuilder html, String indent, int indentLevel)
            throws JSONException {


        if ("meta".equalsIgnoreCase(tagName)) {
            buildMetaTags(obj, html, indentLevel);
            return;
        }

        String attributes = HtmlUtils.formatAttributes(obj.optJSONObject("attributes"));

        if (SelfClosingTag.contains(tagName)) {
            // For self-closing tags, treat all properties as attributes if no explicit attributes
            if (!obj.has("attributes")) {
                attributes = HtmlUtils.formatAttributes(obj);
            }
            html.append(indent).append("<").append(tagName).append(attributes).append(">\n");
        } else {
            // Regular container tag
            html.append(indent).append("<").append(tagName).append(attributes).append(">\n");

            // Build child elements
            for (Iterator<String> it = obj.keys(); it.hasNext();) {
                String childKey = it.next();
                if (isReservedKey(childKey)) {
                    buildTag(childKey, obj.get(childKey), html, indentLevel + 1);
                }
            }

            html.append(indent).append("</").append(tagName).append(">\n");
        }
    }

    private void buildArrayTags(String tagName, JSONArray array, StringBuilder html, int indentLevel)
            throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            buildTag(tagName, array.get(i), html, indentLevel);
        }
    }

    private void buildMetaTags(JSONObject metaObj, StringBuilder html, int indentLevel) throws JSONException {
        String indent = HtmlUtils.createIndent(indentLevel);

        for (Iterator<String> it = metaObj.keys(); it.hasNext();) {
            String attributeName = it.next();
            Object value = metaObj.get(attributeName);

            if (value instanceof String) {
                // Simple attribute like charset
                html.append(indent)
                        .append("<meta ").append(attributeName).append("=\"")
                        .append(HtmlUtils.escapeAttribute(value.toString())).append("\">\n");
            } else if (value instanceof JSONObject) {
                // Complex attribute like viewport
                String content = buildMetaContent((JSONObject) value);
                html.append(indent)
                        .append("<meta name=\"").append(attributeName)
                        .append("\" content=\"").append(HtmlUtils.escapeAttribute(content)).append("\">\n");
            }
        }
    }

    private String buildMetaContent(JSONObject contentObj) throws JSONException {
        StringBuilder content = new StringBuilder();
        for (Iterator<String> it = contentObj.keys(); it.hasNext();) {
            String key = it.next();
            String value = contentObj.getString(key);

            if (content.length() > 0) {
                content.append(", ");
            }
            content.append(key).append("=").append(value);
        }
        return content.toString();
    }

    private boolean isReservedKey(String key) {
        return !"attributes".equals(key);
    }
}