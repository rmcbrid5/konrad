package com.konrad.mail.api;

import com.google.api.client.util.Data;
import com.google.gson.internal.LinkedTreeMap;
import com.konrad.mail.models.Label;

import java.util.ArrayList;
import java.util.List;

public class LabelsResponse {

    /**
     * List of labels.
     * The value may be {@code null}.
     */
    private List<LinkedTreeMap> labels;

    static {
        // hack to force ProGuard to consider Label used, since otherwise it would be stripped out
        // see https://github.com/google/google-api-java-client/issues/543
        Data.nullOf(com.google.api.services.gmail.model.Label.class);
    }

    /**
     * List of labels.
     * @return value or {@code null} for none
     */
    public List<LinkedTreeMap> getLabels() {
        return labels;
    }

    /**
     * List of labels.
     * @param labels labels or {@code null} for none
     */
    public LabelsResponse setLabels(java.util.List<LinkedTreeMap> labels) {
        this.labels = labels;
        return this;
    }

    public List<Label> parseLabelList(){
        ArrayList<Label> labelList = new ArrayList<>();
        for(LinkedTreeMap ltm: labels){
            labelList.add(new Label(ltm));
        }
        return labelList;
    }

}
