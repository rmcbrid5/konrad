package com.konrad.mail.api;

import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

public class MessageIdsResponse {

    /**
     * List of messages.
     * The value may be {@code null}.
     */
    private List<LinkedTreeMap> messages;

    /**
     * List of messages.
     * @return value or {@code null} for none
     */
    public List<LinkedTreeMap> getMessages() {
        return messages;
    }


}

