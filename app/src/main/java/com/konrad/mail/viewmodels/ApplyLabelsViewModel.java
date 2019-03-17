package com.konrad.mail.viewmodels;

import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.view.IApplyLabelsView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyLabelsViewModel {

    private List<Label> userLabels = new ArrayList<>();
    private List<Message> selectedMessages;
    private IApplyLabelsView view;

    private Callback<Void> applyLabelChangesCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            view.close();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            view.showUpdateLabelsError();
        }
    };

    public ApplyLabelsViewModel(IApplyLabelsView view) {
        this.view = view;
    }

    public void setSelectedMessages(List<Message> selectedMessages) {
        this.selectedMessages = selectedMessages;
    }

    /*
    TODO: For Question #6, figure out how to determine which labels would need to be removed from any of the selected messages given the state of the labels being checked or unchecked
     */
    private List<String> parseLabelsToRemove(List<String> labelIds) {
        return new ArrayList<>();
    }

    /*
    TODO: For Question #6, how do we determine what labels to add?
     */
    private List<String> parseLabelsToAdd(List<String> labelIds) {
        return new ArrayList<>();
    }

    /**
     * TODO: For Question #6, You'll want to instantiate a BatchModifyLabelsBody object here with the proper parameters - the class already exists, just create an instance of it here with the proper arguments, and then call GoogleServicesManager's applyLabelChangesToMessages with the callback at the top of this class
     * @param checkedLabelIds - list of the checkedLabelIds from the adapter
     *
     */
    public void applyLabelChanges(List<String> checkedLabelIds) {
        /* TODO: For Question #6, put your code in here and delete the line below! */
        applyLabelChangesCallback.onResponse(null, null);
    }

    public void setUserLabels(List<Label> userList) {
        this.userLabels = userList;
    }

    public List<Label> getUserLabels() {
        return userLabels;
    }

    public List<Message> getSelectedMessages() {
        return selectedMessages;
    }

    private List<String> getSelectedMessageIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Message message : selectedMessages) {
            ids.add(message.getId());
        }
        return ids;
    }
}
