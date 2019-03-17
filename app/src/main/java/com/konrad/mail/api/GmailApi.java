package com.konrad.mail.api;

import com.google.api.services.gmail.model.Label;
import com.konrad.mail.models.Message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GmailApi {

    @GET("{userid}/messages")
    Call<MessageIdsResponse> getAllMessagesWithLabels(@Path("userid") String userId,
                                                      @Query("labelIds") String[] labelsIds);

    @GET("{userid}/messages/{messageid}")
    Call<Message> getMessage(@Path("userid") String userId,
                             @Path("messageid") String messageId);

    @GET("{userid}/labels")
    Call<LabelsResponse> getAllLabels(@Path("userid") String userId);

    @POST("{userid}/labels")
    Call<Label> addLabel(@Path("userid") String userId,
                         @Body Label label);

    @POST("{userid}/messages/batchModify")
    Call<Void> batchModifyMessages(@Path("userid") String userId,
                                   @Body BatchModifyLabelsBody body);

}
