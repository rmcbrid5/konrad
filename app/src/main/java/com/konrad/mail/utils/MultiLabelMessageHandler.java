package com.konrad.mail.utils;

import android.support.annotation.NonNull;

import com.konrad.mail.api.GoogleServicesManager;
import com.konrad.mail.api.MessageIdsResponse;
import com.konrad.mail.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TODO: For Question #2, put your whole implementation here - this class is a placeholder for handling receiving all the messageId list responses and then queueing up all the fetchMessage calls that will need to be performed to get all the messages for all the selected labels, and then it should combine all those messages using MessageUtils, and perform the callback to the original Callback from the viewmodel which is passed in from the constructor
 *
 * Here is how this class should work:
 * 1) This handler provides the calling GoogleServicesManager.fetchMessagesForSelectedIds with a callback for when it has finished each API request to get the messageIds for the selected label.
 * 2) For each of the MessageIdsReponse objects that comes back from those API requests, we need to fire off a new API request to fetch the message data (GoogleServicesManager.fetchMessages - the callback for this needs to be implemented here.
 * 3) Remember that each message we receive needs to be associated with the labelId for which we fetched the messageId list - this is important because we need these lists to perform the merging. How do we store each list of messages and associate the list with the corresponding labelId?
 * 4) Once all the messages for each messageId list have been retrieved, we need to sort those lists with the provided sort method in MessageUtils, and then call the helper function MessageUtils.combineSortedMessages(List<List<Message>>) on those lists to give us back a single list of messages.
 * 5) Now that we have our list of sorted messages, we send it back to the viewmodel by calling the onSuccess method of the messagesCallback object that was provided to this class in the constructor.
 */
public class MultiLabelMessageHandler {

    //Hint: For step 3) above, how can we use this internal class to keep track of the label id that the messages are associated with?
    public static class MessageIdsCallback implements Callback<MessageIdsResponse> {

        //Hint: Maybe a custom Constructor would be helpful

        @Override
        public void onResponse(Call<MessageIdsResponse> call, Response<MessageIdsResponse> response) {

        }

        @Override
        public void onFailure(Call<MessageIdsResponse> call, Throwable t) {

        }
    }

    public MultiLabelMessageHandler(@NonNull BasicCallback<List<Message>> messagesCallback, @NonNull String[] labelIds) {
        //Hint: What do we need to set up in this constructor? What do we need to initialize and how do we track the messages against the labels they're associated with?
    }

    /*
        TODO: For Question #2, this method is provided for you, and the GoogleServicesManager call to fetch the messages already defines how this particular callback will be used. How do we provide the right messageIdsCallback based on a labelId input?
     */
    public MessageIdsCallback getMessageIdsCallback(String labelId) {
        return new MessageIdsCallback();
    }

}
