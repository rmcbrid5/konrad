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
import android.widget.TextView;

import com.konrad.mail.R;
import com.konrad.mail.adapters.LabelSelectorAdapter;
import com.konrad.mail.main.MainActivity;
import com.konrad.mail.models.Label;
import com.konrad.mail.view.IFilterByLabelView;
import com.konrad.mail.viewmodels.FilterByLabelViewModel;

import java.util.List;

public class FilterByLabelFragment extends Fragment implements IFilterByLabelView {

    public FilterByLabelFragment() {
        viewModel = new FilterByLabelViewModel(this);
    }

    private FilterByLabelViewModel viewModel;

    private RecyclerView labelsRecyclerView;
    private LabelSelectorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        view.findViewById(R.id.apply_label_cancel_button).setOnClickListener(v -> close());
        view.findViewById(R.id.apply_label_done_button).setOnClickListener(v -> close());

        labelsRecyclerView = view.findViewById(R.id.change_labels_recyclerview);
        labelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        TextView tv = view.findViewById(R.id.change_labels_main_title);
        tv.setText(getString(R.string.multi_label_select_title));
    }

    public FilterByLabelViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void setLabels(List<Label> labels) {
        viewModel.setUserLabels(labels);
    }

    @Override
    public void close() {
        ((MainActivity)getActivity()).hideFilterByLabel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new LabelSelectorAdapter(viewModel.getFilteredLabelIds());
        adapter.updateLabels(viewModel.getUserLabels());
        labelsRecyclerView.setAdapter(adapter);

    }

    @Override
    public List<String> getSelectedLabels() {
        return adapter.getCheckedLabelIds();
    }

    @Override
    public void setFilteredLabelIds(List<String> filteredLabelIds) {
        viewModel.setFilteredLabelIds(filteredLabelIds);
    }
}
