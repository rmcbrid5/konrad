package com.konrad.mail.view;

import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.IContextProvider;

import java.util.List;

public interface IInboxView extends IContextProvider {

    void showLoadingView();
    void hideLoadingView();

    void setLabels(List<Label> labels);
    void setFilteredLabelIds(List<String> labelIds);
    void showLabelFetchError();
    void showInboxFetchError();
    void showNoLabels();
    void showInboxEmpty();
    void updateMessages(List<Message> messages);
    void updateTitle(String title);
}
