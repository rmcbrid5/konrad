package com.konrad.mail.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.konrad.mail.models.Message;

import java.util.ArrayList;
import java.util.List;

public interface MessageUtils {

    /**
     * TODO: For Question #3 - This helper function to handle merging an indeterminate number of message lists into a single list. You shouldn't need to change this, but you can if you want! This just helps with how your combineSortedMessages(List1, List2) implementation should be called. You should call this method from somewhere inside MultiLabelMessageHandler, when your implementation has finished fetching all the lists of messages and is ready to start combining them.
     * @param listsOfMessages
     * @return single list of Messages that has no duplicates, ordered by date
     */
    static List<Message> combineSortedMessages(@NonNull List<List<Message>> listsOfMessages) {
        ArrayList<Message> result = new ArrayList<>();
        if (listsOfMessages.isEmpty()) return result;
        if (listsOfMessages.size() < 2) return listsOfMessages.get(0);
        int index = 0;
        while (index < listsOfMessages.size()) {
            List<Message> merged = combineSortedMessages(result, listsOfMessages.get(index));
            result.clear();
            result.addAll(merged);
            index++;
        }

        return result;
    }

    /**
     * TODO: For Question #3 - You'll need to implement this method properly. The starter code here just takes all the entries from both lists and returns it. This is NOT WHAT WE WANT
     * @param list1 Sorted list of messages
     * @param list2 Sorted list of messages
     * @return single list of Messages which combines and de-dupes the contents of list1 and list2
     */
    static List<Message> combineSortedMessages(@NonNull List<Message> list1, @NonNull List<Message> list2) {
        //compare all messages in the lists to only get unique messages, if message is unique, add it to the list
        //can use a hashmap for this - store message id as key and make true if it is found then add all messages that have true in the hashmap to the combined list
        ArrayList<Message> combined = new ArrayList<>();
        combined.addAll(list1);
        combined.addAll(list2);
        return combined;
    }

    static void sortMessages(List<Message> messages) {
        messages.sort((o1, o2) -> {
            String id1 = o1.getInternalDate();
            String id2 = o2.getInternalDate();
            return id2.compareTo(id1);
        });
    }

    static String getPrettySender(String originalSender) {
        if (TextUtils.isEmpty(originalSender)) return "";
        int emailStartIndex = originalSender.indexOf("<");
        String tempSender = originalSender;
        if (emailStartIndex != -1) {
            tempSender = originalSender.substring(0, emailStartIndex - 1);
        }

        return tempSender.replace("\"", "");
    }

    static String getPrettySnippet(String originalSnippet, int snippetLength) {
        if (TextUtils.isEmpty(originalSnippet)) return "";
        String prettySnippet;

        prettySnippet = originalSnippet.substring(0, Math.min(originalSnippet.length(), snippetLength));
        if (prettySnippet != "") {
            prettySnippet = prettySnippet + "...";
        }
        return prettySnippet;
    }

}
