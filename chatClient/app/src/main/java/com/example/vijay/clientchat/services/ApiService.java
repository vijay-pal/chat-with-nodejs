package com.example.vijay.clientchat.services;

import com.example.vijay.clientchat.models.FriendRequest;
import com.example.vijay.clientchat.models.RegistrationResponse;
import com.example.vijay.clientchat.models.SearchResponse;
import com.example.vijay.clientchat.models.User;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by vijay on 24/8/17.
 */

public class ApiService {
    private Api mApi;

    public ApiService(String BaseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BaseUrl)
            .build();

        mApi = retrofit.create(Api.class);
    }

    public Api getApi() {
        return mApi;
    }

    public interface Api {

        @POST("/api/v1/register")
        Observable<RegistrationResponse> register(@Body User user);

        @GET("/api/v1/users/{id}")
        Observable<SearchResponse> searchPeople(@Path("id") String id/*, @Query("query") String query*/);

        @POST("/api/v1/add_friend")
        Observable<FriendRequest.Response> sendFriendRequest(@Body FriendRequest.Request request);

        @PUT("/api/v1//friend_request/{id}/confirmation")
        Observable<FriendRequest.Response> confirmFriendRequest(@Path("id") String id, @Body FriendRequest.Confirmation confirmation);
    }
}
