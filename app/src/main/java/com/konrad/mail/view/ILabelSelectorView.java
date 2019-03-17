package com.konrad.mail.view;

import com.konrad.mail.models.Label;
import com.konrad.mail.utils.IContextProvider;

import java.util.List;

public interface ILabelSelectorView extends IContextProvider {
    void setLabels(List<Label> labels);
    void close();
}
