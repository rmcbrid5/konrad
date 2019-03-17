package com.konrad.mail.view;

import com.konrad.mail.models.Message;

import java.util.List;

public interface IApplyLabelsView extends ILabelSelectorView {
    void showUpdateLabelsError();
    void setSelectedMessages(List<Message> selectedMessages);
}
