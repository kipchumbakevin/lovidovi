package com.example.lovidovi.ui;

import android.os.Bundle;

import com.example.lovidovi.R;
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

public class MainActivity extends AppCompatActivity {
    ImageView home,inboxImage,secretImage,notImg;
    RelativeLayout inbox,secret,notification;
    FrameLayout frameLayout;
    NotificationsFragment notificationsFragment;

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
        notImg = findViewById(R.id.notiImage);
        frameLayout = findViewById(R.id.fragments);
        notificationsFragment = new NotificationsFragment();

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
    }

    private void secretLoad() {
        secretImage.setColorFilter(getResources().getColor(R.color.colorTab));
        notImg.setColorFilter(getResources().getColor(R.color.colorWhite));
        inboxImage.setColorFilter(getResources().getColor(R.color.colorWhite));
        home.setColorFilter(getResources().getColor(R.color.colorWhite));
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
