package com.konrad.mail.models;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

public class Message {

    public static final String MESSAGE_SENDER = "From";
    public static final String MESSAGE_SUBJECT = "Subject";

    public Message(String id, Long internalDate) {
        this.id = id;
        this.internalDate = internalDate.toString();
    }

    /**
     * The immutable ID of the message.
     * The value may be {@code null}.
     */
    private String id;

    /**
     * The internal message creation timestamp (epoch ms), which determines ordering in the inbox. For
     * normal SMTP-received email, this represents the time the message was originally accepted by
     * Google, which is more reliable than the Date header. However, for API-migrated mail, it can be
     * configured by client to be based on the Date header.
     * The value may be {@code null}.
     */
    private String internalDate;

    /**
     * List of IDs of labels applied to this message.
     * The value may be {@code null}.
     */
    private List<String> labelIds;

    /**
     * The parsed email structure in the message parts.
     * The value may be {@code null}.
     */
    private LinkedTreeMap payload;


    /**
     * A short part of the message text.
     * The value may be {@code null}.
     */
    private String snippet;

    /**
     * The immutable ID of the message.
     * @return value or {@code null} for none
     */
    public String getId() {
        return id;
    }

    /**
     * The immutable ID of the message.
     * @param id id or {@code null} for none
     */
    public Message setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * The internal message creation timestamp (epoch ms), which determines ordering in the inbox. For
     * normal SMTP-received email, this represents the time the message was originally accepted by
     * Google, which is more reliable than the Date header. However, for API-migrated mail, it can be
     * configured by client to be based on the Date header.
     * @return value or {@code null} for none
     */
    public String getInternalDate() {
        return internalDate;
    }


    /**
     * List of IDs of labels applied to this message.
     * @return value or {@code null} for none
     */
    public List<String> getLabelIds() {
        return labelIds;
    }

    /**
     * A short part of the message text.
     * @return value or {@code null} for none
     */
    public String getSnippet() {
        return snippet;
    }

    public List<Header> parseHeaderList(){
        List<Header> headerList = new ArrayList<>();
        List<LinkedTreeMap> ltmList = (List<LinkedTreeMap>) payload.get("headers");
        for(LinkedTreeMap ltm: ltmList){
            headerList.add(new Header(ltm));
        }
        return headerList;
    }
}
