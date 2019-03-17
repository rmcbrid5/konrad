package com.konrad.mail.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.BasicCallback;
import com.konrad.mail.utils.MultiLabelMessageHandler;
import com.konrad.mail.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Contains all the necessary data for authentication and fetching of messages from the google API.
 */
public class GoogleServicesManager implements GoogleApiClient.OnConnectionFailedListener {

    private static final GoogleServicesManager instance;
    private static final String TAG = "GSM";

    private Retrofit retrofit;
    private GmailApi api;

    private GoogleAccountCredential credential;
    private String currentToken;

    private static final String[] SCOPES = {
            GmailScopes.GMAIL_LABELS,
            GmailScopes.GMAIL_COMPOSE,
            GmailScopes.GMAIL_INSERT,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.GMAIL_READONLY,
            GmailScopes.MAIL_GOOGLE_COM
    };

    private static final String CUR_USER_ID = "me";

    static {
        instance = new GoogleServicesManager();
    }

    private GoogleServicesManager() {
    }

    public static GoogleServicesManager getInstance() {
        return instance;
    }

    private GmailApi getApi() { return api; }

    public void init(Context context) {
        credential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());

    }

    public void login(String accountName, Function<Boolean, Void> callback, Function<UserRecoverableAuthException, Void> recoverableAuthFailureCallback) {
        credential.setSelectedAccountName(accountName);
        getCurrentToken(callback, recoverableAuthFailureCallback);
    }

    private void getCurrentToken(Function<Boolean, Void> callback, Function<UserRecoverableAuthException, Void> recoverableAuthFailureCallback) {
        if (credential != null && credential.getSelectedAccountName() != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        currentToken = credential.getToken();
                        setupRetrofit(callback);
                    } catch (UserRecoverableAuthException e) {
                        recoverableAuthFailureCallback.apply(e);
                        Log.e("TAG", e.toString());
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            }.start();
        }
    }

    private void setupRetrofit(Function<Boolean, Void> callback) {
        if (retrofit == null) {
            OkHttpClient.Builder cli = new OkHttpClient.Builder();
            /* TODO: if you want to look at the output from your API calls, feel free to uncomment and change the logging level here if you think that it will help */
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//            cli.addInterceptor(loggingInterceptor);
            cli.interceptors().add(chain -> {
                Request request;
                String formattedHeader = "Bearer " + currentToken;
                try {
                    request = chain.request().newBuilder().addHeader("Authorization", formattedHeader).build();
                } catch (Exception e) {
                    Utils.printErrorMessage(TAG, "Failed to add interceptor on Api", e);
                    request = chain.request().newBuilder().build();
                }
                return chain.proceed(request);
            });
            retrofit = new Retrofit.Builder().client(cli.build()).baseUrl("https://www.googleapis.com/gmail/v1/users/").addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(GmailApi.class);
        }

        callback.apply(isLoggedIn());
    }

    public void fetchLabels(Callback<LabelsResponse> labelsResponseCallback) {
        Call<LabelsResponse> labelListCall = getInstance().api.getAllLabels(CUR_USER_ID);
        labelListCall.enqueue(labelsResponseCallback);
    }

    public void fetchMessagesForLabel(Callback<MessageIdsResponse> callback, String labelId) {
        String[] labelIdArr = {labelId};
        fetchMessagesForLabels(callback, labelIdArr);
    }

    /*
      NOTE: while this method exists, it does not serve the purpose we need for Questions 2/3. This call only returns messages with *all* the relevant labels applied. We want messages with ANY of the labels applied, so you should not be using this for Questions 2/3.
     */
    private void fetchMessagesForLabels(Callback<MessageIdsResponse> callback, String[] labelIds) {
        Call<MessageIdsResponse> call = getInstance().getApi().getAllMessagesWithLabels(CUR_USER_ID, labelIds);
        call.enqueue(callback);
    }

    public void fetchMessages(List<String> messageIds, Callback<Message> messageCallback) {
        for (String id : messageIds) {
            Call<Message> messageCall = getInstance().getApi().getMessage(CUR_USER_ID, id);
            messageCall.enqueue(messageCallback);
        }
    }

    /*
    TODO - For Question #2: THIS is the service method that should be called by the viewmodel to fetch all messages for all selected labels in the filter. MultiLabelMessageHandler class is used here to manage all the requests that will need to go out for fetching the messages. You can change this implementation if you'd like, but you should be able to do all your actual logic and handling inside of the MultiLabelMessageHandler class.
     */
    public void fetchMessagesForSelectedLabelIds(BasicCallback<List<Message>> callback, String[] labelIds) {
        MultiLabelMessageHandler multiCallback = new MultiLabelMessageHandler(callback, labelIds);
        for (String labelId : labelIds) {
            fetchMessagesForLabel(multiCallback.getMessageIdsCallback(labelId), labelId);
        }
    }

    public void applyLabelChangesToMessages(BatchModifyLabelsBody body, Callback<Void> callback) {
        Call<Void> batchModifyCall = getInstance().getApi().batchModifyMessages(CUR_USER_ID, body);
        batchModifyCall.enqueue(callback);
    }

    public void createLabel(String newLabelName, Callback<Label> updateLabelsCallback) {

        Label label = new Label().setName(newLabelName);
        label.setLabelListVisibility(ApiConstants.LABEL_LIST_VISIBILITY);
        label.setMessageListVisibility(ApiConstants.MESSAGE_LIST_VISIBILITY);
        Call<Label> labelCall = api.addLabel(CUR_USER_ID, label);
        labelCall.enqueue(updateLabelsCallback);

    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(credential.getSelectedAccountName());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        //need to override for client
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }
}
