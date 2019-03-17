package com.konrad.mail.api;

import java.util.List;

public class ModifyLabelsBody {

    private String[] addLabelIds = {};
    private String[] removeLabelIds = {};

    public ModifyLabelsBody(List<String> toBeAdded, List<String> toBeRemoved){
        addLabelIds = toBeAdded.toArray(addLabelIds);
        removeLabelIds = toBeRemoved.toArray(removeLabelIds);
    }
}
