package com.konrad.mail.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konrad.mail.R;
import com.konrad.mail.adapters.LabelSelectorAdapter;
import com.konrad.mail.main.MainActivity;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.Utils;
import com.konrad.mail.view.IApplyLabelsView;
import com.konrad.mail.viewmodels.ApplyLabelsViewModel;

import java.util.List;

/**
  TODO: For Question #6, this is the UI Fragment for Applying Label changes to Messages - You shouldn't need to change anything here, but feel free to do so if your implementation requires it.
 */
public class ApplyLabelsFragment extends Fragment implements IApplyLabelsView {

    private ApplyLabelsViewModel viewModel;

    public ApplyLabelsFragment() {
        viewModel = new ApplyLabelsViewModel(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.label_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView labelsRecyclerView = view.findViewById(R.id.change_labels_recyclerview);
        labelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LabelSelectorAdapter adapter = new LabelSelectorAdapter();
        adapter.setSelectedMessages(viewModel.getSelectedMessages());
        adapter.updateLabels(viewModel.getUserLabels());
        labelsRecyclerView.setAdapter(adapter);

        view.findViewById(R.id.apply_label_cancel_button).setOnClickListener(v -> {
            close();
        });

        view.findViewById(R.id.apply_label_done_button).setOnClickListener(v -> {
            viewModel.applyLabelChanges(adapter.getCheckedLabelIds());
        });
    }

    @Override
    public void setSelectedMessages(List<Message> selectedMessages) {
        viewModel.setSelectedMessages(selectedMessages);
    }

    @Override
    public void setLabels(List<Label> labels) {
        viewModel.setUserLabels(labels);
    }

    @Override
    public void showUpdateLabelsError() {
        Utils.showToast(getContext(), R.string.toast_error_add_label_fail);
    }

    /**
     * Close this Fragment which will reload the inbox with the applied changes
     */
    @Override
    public void close() {
        if (getActivity() != null) ((MainActivity)getActivity()).hideChangeLabel();
    }
}
