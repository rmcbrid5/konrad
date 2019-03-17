package com.konrad.mail.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.konrad.mail.R;
import com.konrad.mail.models.Header;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.MessageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MessageViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    //TODO - For Question #1 - this list will be useful
    private List<Message> currentlySelectedMessages = new ArrayList<>();

    private List<Label> labels = new ArrayList<>();
    private MessageSelectionChangedListener messageSelectionChangedListener;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void updateMessages(List<Message> messages) {
        this.messageList = messages;
        currentlySelectedMessages.clear();
        MessageUtils.sortMessages(messageList);
        notifyDataSetChanged();
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*
         * TODO: For Question #5, you'll want to inflate your new layout file for inflation here. Most of the view binding is already done so you probably want to keep all the view ids the same in your layout. Replace the reference to ugly_message_tile with pretty_message_tile and fill in the layout in that xml file.
         */
        View tileView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pretty_message_tile, viewGroup, false);
        return new MessageViewHolder(tileView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position) {

        Message message = messageList.get(position);
        viewHolder.itemView.setOnClickListener((view) -> {
            if (currentlySelectedMessages.contains(messageList.get(position))) {
                currentlySelectedMessages.remove(messageList.get(position));
                view.setBackgroundColor(Color.WHITE);
                messageSelectionChangedListener.messageSelectionChanged();
            } else {
                currentlySelectedMessages.add(messageList.get(position));
                view.setBackgroundColor(Color.LTGRAY);
                messageSelectionChangedListener.messageSelectionChanged();
            }
        });

        viewHolder.message.setText(MessageUtils.getPrettySnippet(message.getSnippet(), 40));

        List<Header> headerList = message.parseHeaderList();

        //parse headers for useful info
        for (int index = 0; index < headerList.size(); index++) {
            String curHeader = headerList.get(index).getName();
            if (Message.MESSAGE_SUBJECT.equals(curHeader)) {
                String subjectSnippet = MessageUtils.getPrettySnippet(headerList.get(index).getValue(), 40);
                if (!TextUtils.isEmpty(subjectSnippet)) {
                    viewHolder.subject.setText(subjectSnippet);
                } else {
                    viewHolder.subject.setText(R.string.message_subject_empty);
                }
            } else if (Message.MESSAGE_SENDER.equals(curHeader)) {
                String prettySender = MessageUtils.getPrettySender(headerList.get(index).getValue());
                if (!TextUtils.isEmpty(prettySender)) {
                    viewHolder.sender.setText(prettySender);
                    viewHolder.senderInitial.setText(prettySender.substring(0,1));
                } else {
                    viewHolder.sender.setText(R.string.message_sender_unknown);
                    viewHolder.senderInitial.setText("?");
                }
            }
        }
        if (currentlySelectedMessages.contains(message)) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
        String date = sdf.format(new Date(Long.parseLong(message.getInternalDate())));

        viewHolder.date.setText(date);

        viewHolder.labelContainer.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(10);
        for (String labelId : message.getLabelIds()) {
            for (Label label : labels) {
                if (labelId.equals(label.getId())) {
                    /* TODO: For Question #5, you'll want to replace this Textview with your own setup for the label with the rounded corner background. The setting of the text is provided here for you just as an example. */
                    TextView tv = new TextView(viewHolder.itemView.getContext());
                    tv.setText(label.getName());
                    tv.setBackgroundResource(R.drawable.label_background);
                    tv.setLayoutParams(params);
                    viewHolder.labelContainer.addView(tv);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setOnMessageSelectionChangedListener(MessageSelectionChangedListener listener) {
        this.messageSelectionChangedListener = listener;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<Message> getCurrentlySelectedMessages() {
        return currentlySelectedMessages;
    }

    /**
     * TODO: For question #1, this method might be useful to you
     * @return if all messages are selected
     */
    public boolean areAllMessagesSelected() {
        return currentlySelectedMessages.size() == messageList.size();
    }

    /**
     * TODO: For question #1, this method might be useful to you
     * @return if any of the messages in the adapter are currently selected
     */
    public boolean areSomeMessagesSelected() {
        return !currentlySelectedMessages.isEmpty();
    }

    /**
     * TODO: For question #1 - Implement this method - apply
     * This is the method to handle the select all checkbox as it pertains to updating items in the
     * adapter.
     */
    public void handleSelectAll() {
        if(areAllMessagesSelected()||areSomeMessagesSelected()) {
            currentlySelectedMessages.clear();
            messageSelectionChangedListener.messageSelectionChanged();
            notifyDataSetChanged();
        }
        else{
            currentlySelectedMessages = messageList;
            messageSelectionChangedListener.messageSelectionChanged();
            notifyDataSetChanged();
        }
    }

    public interface MessageSelectionChangedListener {
        void messageSelectionChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView senderInitial;
        private TextView message;
        private TextView subject;
        private TextView sender;
        private TextView date;
        private LinearLayout labelContainer;

        MessageViewHolder(View itemView) {
            super(itemView);
            senderInitial = itemView.findViewById(R.id.tv_sender_initial);
            subject = itemView.findViewById(R.id.tv_subject);
            message = itemView.findViewById(R.id.tv_message);
            sender = itemView.findViewById(R.id.tv_sender);
            date = itemView.findViewById(R.id.tv_date);
            labelContainer = itemView.findViewById(R.id.label_container);
        }

    }

}
