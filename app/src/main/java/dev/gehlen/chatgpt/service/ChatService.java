package dev.gehlen.chatgpt.service;

import dev.gehlen.chatgpt.model.ChatResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatService {
    @Headers("Authorization: Bearer " + "API_KEY")
    @POST("completions")
    @FormUrlEncoded
    Call<ChatResponse> sendMessage(@Field("message") String message);
}