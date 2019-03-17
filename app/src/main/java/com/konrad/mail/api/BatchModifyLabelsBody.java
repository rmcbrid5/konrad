package com.konrad.mail.api;

import java.util.List;


/**
 * API Request body for applying a batch of label changes to a list of messages.
 */
public class BatchModifyLabelsBody {

    private String[] ids = {};
    private String[] addLabelIds = {};
    private String[] removeLabelIds = {};

    /**
     *
     * TODO: you don't need to worry about whether the messages already have any of the labels in either list applied to them. This request applies whatever changes are possible to be applied
     *
     * @param messageIds list of message Ids - ALL THESE MESSAGES WILL BE AFFECTED BY THE CHANGES
     * @param toBeAdded list of all labels to be added to each message in messageIds
     * @param toBeRemoved list of all labels to be removed from each message in messageIds
     *
     * RULE - there cannot be any overlap in labelIds between the toBeAdded and toBeRemoved lists
     */
    public BatchModifyLabelsBody(List<String> messageIds, List<String> toBeAdded, List<String> toBeRemoved){
        ids = messageIds.toArray(ids);
        addLabelIds = toBeAdded.toArray(addLabelIds);
        removeLabelIds = toBeRemoved.toArray(removeLabelIds);
    }
}
