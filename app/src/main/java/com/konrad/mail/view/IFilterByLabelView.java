package com.konrad.mail.view;

import java.util.List;

public interface IFilterByLabelView extends ILabelSelectorView {
    List<String> getSelectedLabels();
    void setFilteredLabelIds(List<String> filteredLabelIds);
}
