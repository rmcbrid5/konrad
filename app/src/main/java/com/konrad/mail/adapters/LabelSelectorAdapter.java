package com.konrad.mail.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.konrad.mail.R;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LabelSelectorAdapter extends RecyclerView.Adapter<LabelSelectorAdapter.SelectableLabelViewHolder> {

    private List<Label> labels = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private Set<String> checkedLabelIds = new HashSet<>();
    private boolean isFilterMode = false;

    public LabelSelectorAdapter() {}

    public LabelSelectorAdapter(List<String> filteredLabelIds) {
        this.isFilterMode = true;
        checkedLabelIds.clear();
        checkedLabelIds.addAll(filteredLabelIds);
    }

    public void setSelectedMessages(List<Message> messages) {
        checkedLabelIds.clear();
        this.messages = messages;
    }

    public void updateLabels(List<Label> labels) {
        this.labels = labels;
        if (!isFilterMode) {
            for (Label label : labels) {
                if (isLabelOnAllSelectedMessages(label.getId())) {
                    checkedLabelIds.add(label.getId());
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    @NonNull
    @Override
    public SelectableLabelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View tileView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.label_change_tile, viewGroup, false);

        return new SelectableLabelViewHolder(tileView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectableLabelViewHolder viewHolder, int i) {

        viewHolder.titleTV.setText(labels.get(i).getName());
        viewHolder.checkBox.setChecked(checkedLabelIds.contains(labels.get(i).getId()));

        if (Objects.equals(viewHolder.titleTV.getText(), viewHolder.itemView.getContext().getString(R.string.inbox_title))) {
            viewHolder.icon.setImageResource(R.drawable.mail_icon_dark);
        } else {
            viewHolder.icon.setImageResource(R.drawable.label_icon_dark);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (viewHolder.checkBox.isChecked()) {
                checkedLabelIds.remove(labels.get(i).getId());
            } else {
                checkedLabelIds.add(labels.get(i).getId());
            }
            viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());
        });
    }

    private boolean isLabelOnAllSelectedMessages(String labelId) {
        for (Message message : messages) {
            if (!message.getLabelIds().contains(labelId)) {
                return false;
            }
        }
        return true;
    }


    public ArrayList<String> getCheckedLabelIds() {
        return new ArrayList<>(checkedLabelIds);
    }

    public class SelectableLabelViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView titleTV;
        private ImageView icon;

        SelectableLabelViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.change_label_text);
            checkBox = itemView.findViewById(R.id.change_label_checkbox);
            icon = itemView.findViewById(R.id.change_label_icon);
            checkBox.setClickable(false);
        }
    }
}
