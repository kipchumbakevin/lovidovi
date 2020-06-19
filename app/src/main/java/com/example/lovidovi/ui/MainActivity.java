package com.example.lovidovi.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.lovidovi.R;
import com.example.lovidovi.auth.LoginActivity;
import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.models.SignUpMessagesModel;
import com.example.lovidovi.models.UnreadNotificationsModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.settings.SettingsActivity;
import com.example.lovidovi.utils.SharedPreferencesConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ImageView home,inboxImage,secretImage,notImg;
    RelativeLayout inbox,secret,notification;
    FrameLayout frameLayout;
    NotificationsFragment notificationsFragment;
    TextView unreadInbox,unreadSecret,unreadNot;
    SharedPreferencesConfig sharedPreferencesConfig;
    UnreadNotificationsModel unreadNotificationsModel;
    private Boolean reset = false;
    String gg = "nn";
    private static final String MESS ="com.example.lovidovi.ui";
    int dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        home = findViewById(R.id.home);
        inbox = findViewById(R.id.inbox);
        secret = findViewById(R.id.secret);
        notification = findViewById(R.id.notifications);
        inboxImage = findViewById(R.id.inboxImage);
        secretImage = findViewById(R.id.secretImage);
        unreadInbox = findViewById(R.id.unreadInbox);
        unreadSecret = findViewById(R.id.unreadSecret);
        unreadNot = findViewById(R.id.unreadNoti);
        notImg = findViewById(R.id.notiImage);
        frameLayout = findViewById(R.id.fragments);
        unreadNotificationsModel = new UnreadNotificationsModel();
        sharedPreferencesConfig = new SharedPreferencesConfig(MainActivity.this);
        notificationsFragment = new NotificationsFragment();
        if (getIntent().hasExtra(MESS)){
            reset = getIntent().getBooleanExtra(MESS, false);
        }
        unreadNo();
        unreadIn();
        unreadSe();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeload();
                unreadNo();
                unreadIn();
                unreadSe();
            }
        });
        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secretLoad();
                unreadNo();
                unreadIn();
                unreadSecret.setVisibility(View.GONE);
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd = 1;
                inboxLoad();
                unreadNo();
                unreadSe();
                unreadInbox.setVisibility(View.GONE);
            }
        });
        if (dd==1){
            inboxLoad();
        }
        if (dd==2){
            homeload();
        }else {
            homeload();
        }
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationLoad();
                unreadIn();
                unreadSe();
                unreadNot.setVisibility(View.GONE);
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void unreadNo() {
        final String phone = sharedPreferencesConfig.readClientsPhone();
        Call<UnreadNotificationsModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .unreadN(phone);
        call.enqueue(new Callback<UnreadNotificationsModel>() {
            @Override
            public void onResponse(Call<UnreadNotificationsModel> call, Response<UnreadNotificationsModel> response) {
                if (response.code()==201) {
                    if (response.body().getNum()>0){
                        unreadNot.setVisibility(View.VISIBLE);
                        unreadNot.setText(response.body().getNum()+"");
                    }


                }
                else {
                    //Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnreadNotificationsModel> call, Throwable t) {
               // Toast.makeText(MainActivity.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void unreadIn() {
        Call<UnreadNotificationsModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .unreadI();
        call.enqueue(new Callback<UnreadNotificationsModel>() {
            @Override
            public void onResponse(Call<UnreadNotificationsModel> call, Response<UnreadNotificationsModel> response) {
                if (response.code()==201) {
                    if (response.body().getNum()>0){
                        unreadInbox.setVisibility(View.VISIBLE);
                        unreadInbox.setText(response.body().getNum()+"");
                    }


                }
                else {
                   // Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnreadNotificationsModel> call, Throwable t) {
                //Toast.makeText(MainActivity.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void unreadSe() {
        Call<UnreadNotificationsModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .unreadS();
        call.enqueue(new Callback<UnreadNotificationsModel>() {
            @Override
            public void onResponse(Call<UnreadNotificationsModel> call, Response<UnreadNotificationsModel> response) {
                if (response.code()==201) {
                    if (response.body().getNum()>0){
                        unreadSecret.setVisibility(View.VISIBLE);
                        unreadSecret.setText(response.body().getNum()+"");
                    }


                }
                else {
                   // Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnreadNotificationsModel> call, Throwable t) {
                //Toast.makeText(MainActivity.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void notificationLoad() {
        notImg.setColorFilter(getResources().getColor(R.color.colorTab));
        inboxImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        secretImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        home.setColorFilter(getResources().getColor(R.color.colorWhite));
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragment,notificationsFragment);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.commit();
        NotificationsFragment fragment = new NotificationsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragments,fragment,fragment.getTag()).commit();
    }

    private void inboxLoad() {
        inboxImage.setColorFilter(getResources().getColor(R.color.colorTab));
        notImg.setColorFilter(getResources().getColor(R.color.colorWhite));
        secretImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        home.setColorFilter(getResources().getColor(R.color.colorWhite));
        MessagesFragment fragment = new MessagesFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragments,fragment,fragment.getTag()).commit();
    }

    private void secretLoad() {
        secretImage.setColorFilter(getResources().getColor(R.color.colorTab));
        notImg.setColorFilter(getResources().getColor(R.color.colorWhite));
        inboxImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        home.setColorFilter(getResources().getColor(R.color.colorWhite));
        SecretMessagesFragment fragment = new SecretMessagesFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragments,fragment,fragment.getTag()).commit();
    }

    private void homeload() {
        home.setColorFilter(getResources().getColor(R.color.colorTab));
        notImg.setColorFilter(getResources().getColor(R.color.colorWhite));
        inboxImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        secretImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        HomeFragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragments,fragment,fragment.getTag()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
       // showProgress();
        Call<SignUpMessagesModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .logOut();
        call.enqueue(new Callback<SignUpMessagesModel>() {
            @Override
            public void onResponse(Call<SignUpMessagesModel> call, Response<SignUpMessagesModel> response) {
              //  hideProgress();
                if (response.code() == 200) {
                    sharedPreferencesConfig.clear();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("logout", sharedPreferencesConfig.readClientsAccessToken());

                } else {
                    Toast.makeText(MainActivity.this, "response:" + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SignUpMessagesModel> call, Throwable t) {
           //     hideProgress();
                Toast.makeText(MainActivity.this, "errot:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
