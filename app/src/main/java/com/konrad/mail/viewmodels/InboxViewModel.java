package com.konrad.mail.viewmodels;

import android.support.annotation.NonNull;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.internal.LinkedTreeMap;
import com.konrad.mail.api.GoogleServicesManager;
import com.konrad.mail.api.LabelsResponse;
import com.konrad.mail.api.MessageIdsResponse;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.BasicCallback;
import com.konrad.mail.view.IInboxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxViewModel {

    private GoogleServicesManager googleServicesManager;
    private IInboxView view;

    private List<Label> labels = new ArrayList<>();
    private List<Label> userLabels = new ArrayList<>();
    private List<String> filteredLabelIds = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private String selectedLabelTitle;
    private int messageFetchCount = 0;

    /*
     * TODO: For question #2, this is the final callback that needs to be called for to trigger the Inbox to reload the messages so that the new set of messages from the selected filtered labels comes back. This is already implemented for you, your job is to implement the call chain that leads to this callback being called. Do your work in MultiLabelMessageHandler.java
     */
    private BasicCallback<List<Message>> labelFilteredMessagesCallback = new BasicCallback<List<Message>>() {
        @Override
        public void onSuccess(@NonNull List<Message> response) {
            messages = new ArrayList<>(response);
            updateMessageCounts(messages);
            view.updateMessages(messages);
            view.hideLoadingView();
        }

        @Override
        public void onFailure(Throwable t) {
            view.showInboxFetchError();
            view.hideLoadingView();
        }
    };

    /*
     * Callback for handling the API response for fetching all the labels associated with the user's inbox
     */
    private Callback<LabelsResponse> labelsResponseCallback = new Callback<LabelsResponse>() {

        private void setLabelLists(List<Label> labelList) {
            labels = labelList;
            userLabels = parseUserLabels(labelList);
            view.setLabels(userLabels);
        }

        /**
         * Helper method that reduces the returned list of Labels down into just the labels that the
         * user has defined. We use this user label list to populate other views which allow
         * manipulating labels
         * @param labelList full list of all possible labels which includes categories, inbox, etc.
         * @return sanitized list containing actual user labels
         */
        private ArrayList<Label> parseUserLabels(List<Label> labelList) {

            ArrayList<Label> userList = new ArrayList<>();
            for (int index = 0; index < labelList.size(); index++) {
                if (Objects.equals(labelList.get(index).getType(), Label.DEFAULT_TYPE)) {
                    userList.add(labelList.get(index));
                }
            }
            return userList;
        }

        @Override
        public void onResponse(@NonNull Call<LabelsResponse> call, Response<LabelsResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                LabelsResponse listResponse = response.body();
                if (!listResponse.getLabels().isEmpty()) {
                    setLabelLists(listResponse.parseLabelList());
                }
            } else {
                view.showNoLabels();
                view.hideLoadingView();
            }
        }

        @Override
        public void onFailure(@NonNull Call<LabelsResponse> call, Throwable t) {
            view.showLabelFetchError();
            view.hideLoadingView();
        }
    };

    /*
     * TODO: For Question #2 - this can be used as a basic example of what kind of callback you'll need to handle the messageIds - this isn't exactly going to work for you, but you can model your implementation in MultiLabelMessageHandler after this callback
     * Callback for handling all the messageIds returned for the selected single label when loading messages
     */
    private Callback<MessageIdsResponse> messageIdsCallback = new Callback<MessageIdsResponse>() {
        @Override
        public void onResponse(@NonNull Call<MessageIdsResponse> call, Response<MessageIdsResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                MessageIdsResponse messagesResponse = response.body();
                if (!CollectionUtils.isEmpty(messagesResponse.getMessages())) {
                    messages.clear();
                    messageFetchCount = 0;
                    ArrayList<String> messageIds = new ArrayList<>();
                    for (LinkedTreeMap listObject : messagesResponse.getMessages()) {
                        if (listObject.containsKey("id")) {
                            messageIds.add(listObject.get("id").toString());
                        }
                    }
                    messageFetchCount += messageIds.size();
                    googleServicesManager.fetchMessages(messageIds, messageCallback);
                } else {
                    updateMessageCounts(messages);
                    view.updateMessages(messages);
                    view.showInboxEmpty();
                    view.hideLoadingView();
                }
            } else {
                view.showInboxFetchError();
                view.hideLoadingView();
            }
        }

        @Override
        public void onFailure(@NonNull Call<MessageIdsResponse> call, Throwable t) {
            view.showInboxFetchError();
            view.hideLoadingView();
        }
    };

    /*
     * TODO: For Question #2 - this can be used as a basic example of what kind of callback you'll need to handle the message - this isn't exactly going to work for you, but you can model your implementation in MultiLabelMessageHandler after this callback
     * Callback for handling each individual message response that is fetched from the messageIds callback
     */
    private Callback<Message> messageCallback = new Callback<Message>() {

        @Override
        public void onResponse(@NonNull Call<Message> call, Response<Message> response) {
            if (response.isSuccessful() && response.body() != null) {
                Message message = response.body();
                messages.add(message);
            }
            if (--messageFetchCount == 0) view.hideLoadingView();
            updateMessageCounts(messages);
            view.updateMessages(messages);
        }

        @Override
        public void onFailure(@NonNull Call<Message> call, Throwable t) {
            if (--messageFetchCount == 0) view.hideLoadingView();
        }
    };

    public InboxViewModel(IInboxView view) {
        googleServicesManager = GoogleServicesManager.getInstance();
        this.view = view;
    }

    public void init() {
        labels.clear();
        messages.clear();
        userLabels.clear();
        selectedLabelTitle = "Inbox";
        view.updateTitle(selectedLabelTitle);
        fetchMessages();
    }

    private void fetchMessages() {
        googleServicesManager.fetchLabels(labelsResponseCallback);
        if (filteredLabelIds == null || filteredLabelIds.isEmpty()) {
            googleServicesManager.fetchMessagesForLabel(messageIdsCallback, Label.TYPE_INBOX);
        } else {
            /*
            TODO: For Question #2, this is the entry point into the sequence of API Calls. You should not need to change anything here.
             */
            googleServicesManager.fetchMessagesForSelectedLabelIds(labelFilteredMessagesCallback, filteredLabelIds.toArray(new String[]{}));
        }
    }

    public List<Label> getSortedUserLabelList() {

        Collections.sort(userLabels, (o1, o2) -> {
            String id1str = o1.getId().substring(6);
            long id1 = Long.parseLong(id1str);
            String id2str = o2.getId().substring(6);
            long id2 = Long.parseLong(id2str);
            return Long.compare(id1, id2);
        });

        return userLabels;
    }

    public void handleUserLabelNavItemOnClick(CharSequence title) {
        for (Label label : labels) {
            if (title.toString().equals(label.getName())) {
                getMessagesForLabel(label);
            }
        }
    }

    public void getMessagesForLabel(Label label) {
        googleServicesManager.fetchMessagesForLabel(messageIdsCallback, label.getId());
        selectedLabelTitle = label.getName();
        view.updateTitle(selectedLabelTitle);
    }

    public String getSelectedLabelTitle() {
        return selectedLabelTitle;
    }

    public List<Label> getUserLabels() {
        return userLabels;
    }

    /**
     * TODO: For Question #7 - This method retrieves the inbox message count - Fill in the correct implementation
     * @return the correct number of messages that have the Inbox label
     */
    public int getInboxMessageCount() {
        return -1;
    }

    /**
     * TODO: For Question #7 - This is the method that updates the message counts that you should find in the drawer. Implement this method. It is already being called in all the relevant callbacks, so it just needs to do the work. HINT: Look at the Label.java model - is there a field there that could be helpful?
     *
     * @param messages the list of messages for which counts need to be calculated
     */
    public void updateMessageCounts(List<Message> messages) {

    }

    public void setFilteredLabelIds(List<String> labelIds) {
        this.filteredLabelIds = labelIds;
    }

    public List<String> getFilteredLabelIds() {
        return this.filteredLabelIds;
    }
}
