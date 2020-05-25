package com.example.lovidovi.networking;

import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.models.ReceiveNotificationsModel;
import com.example.lovidovi.models.SendCodeModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.models.UsersModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderInterface {
    @FormUrlEncoded
    @POST("api/auth/login")
    Call<UsersModel> login(
            @Field("username")String username,
            @Field("password")String password
    );
    @FormUrlEncoded
    @POST("api/checkifexist")
    Call<SignUpMessagesModel> checkIfExist(
            @Field("phone")String phoneN,
            @Field("username")String userN
    );
    @FormUrlEncoded
    @POST("api/auth/signup")
    Call<UsersModel> signUp(
            @Field("name")String name,
            @Field("username")String username,
            @Field("phone")String phone,
            @Field("password")String password,
            @Field("password_confirmation")String confirmPassword,
            @Field("code") String code
    );
    @FormUrlEncoded
    @POST("api/signupcode")
    Call<SendCodeModel> code(
            @Field("phone")String phone,
            @Field("appSignature")String appSignature
    );

    //getnotifications
    @FormUrlEncoded
    @POST("api/fetch")
    Call<List<ReceiveNotificationsModel>> getNotifications(
            @Field("phone")String phone
    );
    //notifycrush
    @FormUrlEncoded
    @POST("api/notifycrush")
    Call<SignUpMessagesModel> notifyCrush(
            @Field("phone")String phone
    );

    //getallquotes
    @GET("api/fetchquote")
    Call<List<QuotesModel>> getQuotes();
}
