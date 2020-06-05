package com.ubtech.zhifu.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class XmlToJson {
    private static final String DEFAULT_CONTENT_NAME = "content";
    private static final boolean DEFAULT_EMPTY_BOOLEAN = false;
    private static final double DEFAULT_EMPTY_DOUBLE = 0.0d;
    private static final int DEFAULT_EMPTY_INTEGER = 0;
    private static final long DEFAULT_EMPTY_LONG = 0;
    private static final String DEFAULT_EMPTY_STRING = "";
    private static final String DEFAULT_ENCODING = "utf-8";
    private static final String DEFAULT_INDENTATION = "   ";
    private static final String TAG = "XmlToJson";
    private HashMap<String, String> mAttributeNameReplacements;
    private HashMap<String, String> mContentNameReplacements;
    private HashMap<String, Class> mForceClassForPath;
    private HashSet<String> mForceListPaths;
    private String mIndentationPattern;
    private String mInputEncoding;
    private InputStream mInputStreamSource;
    private JSONObject mJsonObject;
    private HashSet<String> mSkippedAttributes;
    private HashSet<String> mSkippedTags;
    private StringReader mStringSource;

    public static class Builder {
        /* access modifiers changed from: private */
        public HashMap<String, String> mAttributeNameReplacements = new HashMap<>();
        /* access modifiers changed from: private */
        public HashMap<String, String> mContentNameReplacements = new HashMap<>();
        /* access modifiers changed from: private */
        public HashMap<String, Class> mForceClassForPath = new HashMap<>();
        /* access modifiers changed from: private */
        public HashSet<String> mForceListPaths = new HashSet<>();
        /* access modifiers changed from: private */
        public String mInputEncoding = XmlToJson.DEFAULT_ENCODING;
        /* access modifiers changed from: private */
        public InputStream mInputStreamSource;
        /* access modifiers changed from: private */
        public HashSet<String> mSkippedAttributes = new HashSet<>();
        /* access modifiers changed from: private */
        public HashSet<String> mSkippedTags = new HashSet<>();
        /* access modifiers changed from: private */
        public StringReader mStringSource;

        public Builder(@NonNull String xmlSource) {
            this.mStringSource = new StringReader(xmlSource);
        }

        public Builder(@NonNull InputStream inputStreamSource, @Nullable String inputEncoding) {
            this.mInputStreamSource = inputStreamSource;
            this.mInputEncoding = inputEncoding == null ? XmlToJson.DEFAULT_ENCODING : inputEncoding;
        }

        public Builder forceList(@NonNull String path) {
            this.mForceListPaths.add(path);
            return this;
        }

        public Builder setAttributeName(@NonNull String attributePath, @NonNull String replacementName) {
            this.mAttributeNameReplacements.put(attributePath, replacementName);
            return this;
        }

        public Builder setContentName(@NonNull String contentPath, @NonNull String replacementName) {
            this.mContentNameReplacements.put(contentPath, replacementName);
            return this;
        }

        public Builder forceIntegerForPath(@NonNull String path) {
            this.mForceClassForPath.put(path, Integer.class);
            return this;
        }

        public Builder forceLongForPath(@NonNull String path) {
            this.mForceClassForPath.put(path, Long.class);
            return this;
        }

        public Builder forceDoubleForPath(@NonNull String path) {
            this.mForceClassForPath.put(path, Double.class);
            return this;
        }

        public Builder forceBooleanForPath(@NonNull String path) {
            this.mForceClassForPath.put(path, Boolean.class);
            return this;
        }

        public Builder skipTag(@NonNull String path) {
            this.mSkippedTags.add(path);
            return this;
        }

        public Builder skipAttribute(@NonNull String path) {
            this.mSkippedAttributes.add(path);
            return this;
        }

        public XmlToJson build() {
            return new XmlToJson(this);
        }
    }

    private XmlToJson(Builder builder) {
        this.mIndentationPattern = DEFAULT_INDENTATION;
        this.mSkippedAttributes = new HashSet<>();
        this.mSkippedTags = new HashSet<>();
        this.mStringSource = builder.mStringSource;
        this.mInputStreamSource = builder.mInputStreamSource;
        this.mInputEncoding = builder.mInputEncoding;
        this.mForceListPaths = builder.mForceListPaths;
        this.mAttributeNameReplacements = builder.mAttributeNameReplacements;
        this.mContentNameReplacements = builder.mContentNameReplacements;
        this.mForceClassForPath = builder.mForceClassForPath;
        this.mSkippedAttributes = builder.mSkippedAttributes;
        this.mSkippedTags = builder.mSkippedTags;
        this.mJsonObject = convertToJSONObject();
    }

    @Nullable
    public JSONObject toJson() {
        return this.mJsonObject;
    }

    @Nullable
    private JSONObject convertToJSONObject() {
        try {
            Tag parentTag = new Tag("", "xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            setInput(xpp);
            for (int eventType = xpp.getEventType(); eventType != 0; eventType = xpp.next()) {
            }
            readTags(parentTag, xpp);
            unsetInput();
            return convertTagToJson(parentTag, false);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setInput(XmlPullParser xpp) {
        if (this.mStringSource != null) {
            try {
                xpp.setInput(this.mStringSource);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } else {
            try {
                xpp.setInput(this.mInputStreamSource, this.mInputEncoding);
            } catch (XmlPullParserException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void unsetInput() {
        if (this.mStringSource != null) {
            this.mStringSource.close();
        }
    }

    private void readTags(Tag parent, XmlPullParser xpp) {
        int eventType;
        do {
            try {
                eventType = xpp.next();
                if (eventType == 2) {
                    String tagName = xpp.getName();
                    String path = parent.getPath() + "/" + tagName;
                    boolean skipTag = this.mSkippedTags.contains(path);
                    Tag child = new Tag(path, tagName);
                    if (!skipTag) {
                        parent.addChild(child);
                    }
                    int attrCount = xpp.getAttributeCount();
                    for (int i = 0; i < attrCount; i++) {
                        String attrName = xpp.getAttributeName(i);
                        String attrValue = xpp.getAttributeValue(i);
                        String attrPath = parent.getPath() + "/" + child.getName() + "/" + attrName;
                        if (!this.mSkippedAttributes.contains(attrPath)) {
                            Tag attribute = new Tag(attrPath, getAttributeNameReplacement(attrPath, attrName));
                            attribute.setContent(attrValue);
                            child.addChild(attribute);
                        }
                    }
                    readTags(child, xpp);
                } else if (eventType == 4) {
                    parent.setContent(xpp.getText());
                } else if (eventType != 3 && eventType != 1) {
                    Log.i(TAG, "unknown xml eventType " + eventType);
                } else {
                    return;
                }
            } catch (NullPointerException | XmlPullParserException e) {
//                e = e;
                e.printStackTrace();
                return;
            } catch (IOException e2) {
//                e = e2;
//                e.printStackTrace();
                return;
            }
        } while (eventType != 1);
    }

    private JSONObject convertTagToJson(Tag tag, boolean isListElement) {
        JSONObject json = new JSONObject();
        if (tag.getContent() != null) {
            String path = tag.getPath();
            putContent(path, json, getContentNameReplacement(path, DEFAULT_CONTENT_NAME), tag.getContent());
        }
        try {
            for (ArrayList<Tag> group : tag.getGroupedElements().values()) {
                if (group.size() == 1) {
                    Tag child = group.get(0);
                    if (isForcedList(child)) {
                        JSONArray list = new JSONArray();
                        list.put(convertTagToJson(child, true));
                        json.put(child.getName(), list);
                    } else if (child.hasChildren()) {
                        json.put(child.getName(), convertTagToJson(child, false));
                    } else {
                        putContent(child.getPath(), json, child.getName(), child.getContent());
                    }
                } else {
                    JSONArray list2 = new JSONArray();
                    Iterator<Tag> it = group.iterator();
                    while (it.hasNext()) {
                        list2.put(convertTagToJson(it.next(), true));
                    }
                    json.put(group.get(0).getName(), list2);
                }
            }
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void putContent(String path, JSONObject json, String tag, String content) {
        try {
            Class forcedClass = this.mForceClassForPath.get(path);
            if (forcedClass == null) {
                if (content == null) {
                    content = "";
                }
                json.put(tag, content);
            } else if (forcedClass == Integer.class) {
                try {
                    json.put(tag, Integer.valueOf(Integer.parseInt(content)));
                } catch (NumberFormatException e) {
                    json.put(tag, 0);
                }
            } else if (forcedClass == Long.class) {
                try {
                    json.put(tag, Long.valueOf(Long.parseLong(content)));
                } catch (NumberFormatException e2) {
                    json.put(tag, 0);
                }
            } else if (forcedClass == Double.class) {
                try {
                    json.put(tag, Double.valueOf(Double.parseDouble(content)));
                } catch (NumberFormatException e3) {
                    json.put(tag, DEFAULT_EMPTY_DOUBLE);
                }
            } else if (forcedClass != Boolean.class) {
            } else {
                if (content == null) {
                    json.put(tag, false);
                } else if (content.equalsIgnoreCase("true")) {
                    json.put(tag, true);
                } else if (content.equalsIgnoreCase("false")) {
                    json.put(tag, false);
                } else {
                    json.put(tag, false);
                }
            }
        } catch (JSONException e4) {
        }
    }

    private boolean isForcedList(Tag tag) {
        return this.mForceListPaths.contains(tag.getPath());
    }

    private String getAttributeNameReplacement(String path, String defaultValue) {
        String result = this.mAttributeNameReplacements.get(path);
        return result != null ? result : defaultValue;
    }

    private String getContentNameReplacement(String path, String defaultValue) {
        String result = this.mContentNameReplacements.get(path);
        return result != null ? result : defaultValue;
    }

    public String toString() {
        if (this.mJsonObject != null) {
            return this.mJsonObject.toString();
        }
        return null;
    }

    public String toFormattedString(@Nullable String indentationPattern) {
        if (indentationPattern == null) {
            this.mIndentationPattern = DEFAULT_INDENTATION;
        } else {
            this.mIndentationPattern = indentationPattern;
        }
        return toFormattedString();
    }

    public String toFormattedString() {
        if (this.mJsonObject == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        format(this.mJsonObject, builder, "");
        builder.append("}\n");
        return builder.toString();
    }

    private void format(JSONObject jsonObject, StringBuilder builder, String indent) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            builder.append(indent);
            builder.append(this.mIndentationPattern);
            builder.append("\"");
            builder.append(key);
            builder.append("\": ");
            Object value = jsonObject.opt(key);
            if (value instanceof JSONObject) {
                builder.append(indent);
                builder.append("{\n");
                format((JSONObject) value, builder, indent + this.mIndentationPattern);
                builder.append(indent);
                builder.append(this.mIndentationPattern);
                builder.append("}");
            } else if (value instanceof JSONArray) {
                formatArray((JSONArray) value, builder, indent + this.mIndentationPattern);
            } else {
                formatValue(value, builder);
            }
            if (keys.hasNext()) {
                builder.append(",\n");
            } else {
                builder.append("\n");
            }
        }
    }

    private void formatArray(JSONArray array, StringBuilder builder, String indent) {
        builder.append("[\n");
        for (int i = 0; i < array.length(); i++) {
            Object element = array.opt(i);
            if (element instanceof JSONObject) {
                builder.append(indent);
                builder.append(this.mIndentationPattern);
                builder.append("{\n");
                format((JSONObject) element, builder, indent + this.mIndentationPattern);
                builder.append(indent);
                builder.append(this.mIndentationPattern);
                builder.append("}");
            } else if (element instanceof JSONArray) {
                formatArray((JSONArray) element, builder, indent + this.mIndentationPattern);
            } else {
                formatValue(element, builder);
            }
            if (i < array.length() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append(indent);
        builder.append("]");
    }

    private void formatValue(Object value, StringBuilder builder) {
        if (value instanceof String) {
            String string = ((String) value).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", Matcher.quoteReplacement("\\\"")).replaceAll("/", "\\\\/").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replaceAll("\r", "\\\\r");
            builder.append("\"");
            builder.append(string);
            builder.append("\"");
        } else if (value instanceof Long) {
            builder.append((Long) value);
        } else if (value instanceof Integer) {
            builder.append((Integer) value);
        } else if (value instanceof Boolean) {
            builder.append((Boolean) value);
        } else if (value instanceof Double) {
            builder.append((Double) value);
        } else {
            builder.append(value.toString());
        }
    }

}
