package com.example.lovidovi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lovidovi.R;

public class SharedPreferencesConfig {
    private SharedPreferences sharedPreferences;
    private Context context;
    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREFERENCES), Context.MODE_PRIVATE);
    }
    public void saveChangedDetails(String name,String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.CLIENTS_USERNAME),username);
        editor.putString(context.getResources().getString(R.string.CLIENTS_NAME),name);

        editor.commit();
    }
    public void saveAuthenticationInformation(String accessToken,String name,String username, String phone,String status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.CLIENTS_TOKEN),accessToken);
        editor.putString(context.getResources().getString(R.string.CLIENTS_PHONE),phone);
        editor.putString(context.getResources().getString(R.string.CLIENTS_STATUS),status);
        editor.putString(context.getResources().getString(R.string.CLIENTS_NAME),username);
        editor.putString(context.getResources().getString(R.string.CLIENTS_USERNAME),name);

        editor.commit();
        Log.d("shared", accessToken);
    }

    public String readClientsPhone(){
        String phone;
        phone = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_PHONE),"");
        return  phone;
    }
    public String readClientsName(){
        String name;
        name = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_NAME),"");
        return  name;
    }

    public String readClientsUsername(){
        String username;
        username = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_USERNAME),"");
        return  username;
    }

    public String readClientsStatus(){
        String status;
        status = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_STATUS),"");
        return status;
    }
    public String readClientsAccessToken(){
        String accessToken;
        accessToken = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_TOKEN),"");
        return accessToken;
    }
    public  boolean isloggedIn(){
        return readClientsStatus().equals(Constants.ACTIVE_CONSTANT);
    }

    public void clear() {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear()
                .apply();
    }
}
