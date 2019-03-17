package com.konrad.mail.viewmodels;

import com.konrad.mail.models.Label;
import com.konrad.mail.view.IFilterByLabelView;

import java.util.ArrayList;
import java.util.List;

public class FilterByLabelViewModel {

    private List<Label> userLabels = new ArrayList<>();
    private List<String> filteredLabelIds = new ArrayList<>();
    private IFilterByLabelView view;

    public FilterByLabelViewModel(IFilterByLabelView view) {
        this.view = view;
    }

    public void setUserLabels(List<Label> userLabels) {
        this.userLabels = userLabels;
    }

    public List<Label> getUserLabels() {
        return userLabels;
    }

    public void setFilteredLabelIds(List<String> filteredLabelIds) {
        this.filteredLabelIds = filteredLabelIds;
    }

    public List<String> getFilteredLabelIds() {
        return this.filteredLabelIds;
    }
}
