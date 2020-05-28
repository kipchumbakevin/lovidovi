package com.example.lovidovi.ui;

import android.os.Bundle;

import com.example.lovidovi.R;
import com.example.lovidovi.models.QuotesModel;
import com.example.lovidovi.models.UnreadNotificationsModel;
import com.example.lovidovi.networking.RetrofitClient;
import com.example.lovidovi.utils.SharedPreferencesConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        unreadNo();
        homeload();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeload();
            }
        });
        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secretLoad();
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inboxLoad();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationLoad();
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
        Call<List<UnreadNotificationsModel>> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .unreadN(phone);
        call.enqueue(new Callback<List<UnreadNotificationsModel>>() {
            @Override
            public void onResponse(Call<List<UnreadNotificationsModel>> call, Response<List<UnreadNotificationsModel>> response) {
                if (response.code()==201) {
                    unreadNot.setText(unreadNotificationsModel.getNum()+"");
                    Toast.makeText(MainActivity.this,phone + unreadNotificationsModel.getNum()+"",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UnreadNotificationsModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Network error",Toast.LENGTH_SHORT).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
