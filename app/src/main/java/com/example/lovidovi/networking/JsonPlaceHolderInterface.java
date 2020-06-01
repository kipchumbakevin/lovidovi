package com.example.lovidovi.networking;

import com.example.lovidovi.models.ChangeDetailsModel;
import com.example.lovidovi.models.ChangedForgotPassModel;
import com.example.lovidovi.models.ChatsModel;
import com.example.lovidovi.models.ConfirmPhoneChangeCodeModel;
import com.example.lovidovi.models.ForgotPasswordModel;
import com.example.lovidovi.models.GenerateCodeModel;
import com.example.lovidovi.models.MessagesModel;
import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.models.ReceiveNotificationsModel;
import com.example.lovidovi.models.SendCodeModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.models.UnreadNotificationsModel;
import com.example.lovidovi.models.UsersModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    //send message
    @FormUrlEncoded
    @POST("api/sendmsg")
    Call<SignUpMessagesModel> sendM(
            @Field("phone")String phone,
            @Field("msg")String mess
    );
    //fetchmessages
    @FormUrlEncoded
    @POST("api/fetchmsgs")
    Call<List<MessagesModel>> getM(
            @Field("chat_id")String chat_id
    );
    //fetch chats
    @GET("api/fetchchats")
    Call<List<ChatsModel>> getChats();

//    @GET("api/fetchchats")
//    Call<List<ChatsModel>> getChats(@Header("Authorization")String access_token);


    //getnotifications
    @FormUrlEncoded
    @POST("api/fetch")
    Call<List<ReceiveNotificationsModel>> getNotifications(
            @Field("phone")String phone
    );
    //unread notification
    @FormUrlEncoded
    @POST("api/unread")
    Call<UnreadNotificationsModel> unreadN(
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

    //add a quote

    @FormUrlEncoded
    @POST("api/insertquote")
    Call<SignUpMessagesModel> addQuote(
            @Field("quote")String quote
    );

    //secret messages
    @FormUrlEncoded
    @POST("api/sendsecret")
    Call<SignUpMessagesModel> sendsecretM(
            @Field("phone")String simu,
            @Field("msg")String mess
    );
    @FormUrlEncoded
    @POST("api/fetchsecret")
    Call<List<MessagesModel>> getsecretM(
            @Field("chat_id")String chat_id
    );
    @GET("api/fetchsecretchats")
    Call<List<ChatsModel>> getsecretChats();
    //read all notifications
    @FormUrlEncoded
    @POST("api/readnotifications")
    Call<SignUpMessagesModel> readAllN(
            @Field("phone")String phone
    );




    @FormUrlEncoded
    @POST("api/changepassword")
    Call<ChangedForgotPassModel> changePassword(
            @Field("newpass")String newPass,
            @Field("oldpass")String oldPass
    );

    @FormUrlEncoded
    @POST("api/sendcode")
    Call<ForgotPasswordModel> sendForgotCode(
            @Field("phone")String newPass
    );
    @FormUrlEncoded
    @POST("api/newpassword")
    Call<ChangedForgotPassModel> newPass(
            @Field("code")String code,
            @Field("newpass")String newPass,
            @Field("phone")String phone
    );

    @FormUrlEncoded
    @POST("api/changedetails")
    Call<ChangeDetailsModel> changeDetails(
            @Field("username")String changed
    );


    @FormUrlEncoded
    @POST("api/checkphone")
    Call<SignUpMessagesModel> checkIfNoCorrect(
            @Field("oldphone")String oldphone,
            @Field("pas")String pas,
            @Field("newphone")String newnumber
    );

    @FormUrlEncoded
    @POST("api/generatecode")
    Call<GenerateCodeModel> generateCode(
            @Field("oldphone")String oldphone,
            @Field("appSignature")String appSignature
    );
    @FormUrlEncoded
    @POST("api/changephone")
    Call<ConfirmPhoneChangeCodeModel> changePhone(
            @Field("newphone")String newphone,
            @Field("oldphone")String oldphone,
            @Field("code")String code
    );

    @GET("api/auth/logout")
    Call<SignUpMessagesModel> logOut();
}

