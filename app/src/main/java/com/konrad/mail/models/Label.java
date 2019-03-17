package com.konrad.mail.models;

import com.google.gson.internal.LinkedTreeMap;

public class Label {

    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String DEFAULT_TYPE = "user";

    public static final String TYPE_INBOX = "INBOX";
    public static final String TYPE_STARRED = "STARRED";
    public static final String TYPE_TRASH = "TRASH";

    private String name;
    private String type;
    private String id;
    private int totalMessages;

    public Label(LinkedTreeMap ltm) {

        name = ltm.containsKey(KEY_NAME) ? ltm.get(KEY_NAME).toString() : "";
        type = ltm.containsKey(KEY_TYPE) ? ltm.get(KEY_TYPE).toString() : DEFAULT_TYPE;
        id = ltm.containsKey(KEY_ID) ? ltm.get(KEY_ID).toString() : "";
        totalMessages = 0;
    }

    public Label(String name, String id) {
        this.name = name;
        this.id = id;
        type = DEFAULT_TYPE;
    }

    public Label(String name) {
        this(name, TYPE_INBOX);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }
}
