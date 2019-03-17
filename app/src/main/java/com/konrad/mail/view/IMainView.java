package com.konrad.mail.view;

import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.IContextProvider;

import java.util.List;

public interface IMainView extends IContextProvider {
    void showSignIn();
    void showInbox();
    void showAddLabel();
    void hideAddLabel();
    void showChangeLabel(List<Message> selectedMessages, List<Label> labels);
    void hideChangeLabel();
    void showFilterByLabel(List<Label> labels, List<String> filteredLabels);
    void hideFilterByLabel();
}
