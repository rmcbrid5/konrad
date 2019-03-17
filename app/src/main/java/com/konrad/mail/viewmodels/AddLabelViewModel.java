package com.konrad.mail.viewmodels;

import com.google.api.services.gmail.model.Label;
import com.konrad.mail.api.GoogleServicesManager;
import com.konrad.mail.api.LabelsResponse;
import com.konrad.mail.view.IAddLabelView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLabelViewModel {

    private IAddLabelView view;

    private Callback<LabelsResponse> labelsCallback = new Callback<LabelsResponse>() {
        @Override
        public void onResponse(Call<LabelsResponse> call, Response<LabelsResponse> response) {
            view.close();
        }

        @Override
        public void onFailure(Call<LabelsResponse> call, Throwable t) {
            view.showError();
        }
    };

    private Callback<Label> createLabelCallback = new Callback<Label>() {
        @Override
        public void onResponse(Call<Label> call, Response<Label> response) {
            if (response.isSuccessful() & response.body() != null) {
                GoogleServicesManager.getInstance().fetchLabels(labelsCallback);
            }
        }

        @Override
        public void onFailure(Call<Label> call, Throwable t) {
            view.showError();
        }

    };

    public AddLabelViewModel(IAddLabelView view) {
        this.view = view;
    }

    public void createLabel(String newLabelStr) {
        GoogleServicesManager.getInstance().createLabel(newLabelStr, createLabelCallback);
    }
}
