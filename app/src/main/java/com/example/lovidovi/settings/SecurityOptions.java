package com.example.lovidovi.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lovidovi.R;
import com.example.lovidovi.ui.MainActivity;

public class SecurityOptions extends AppCompatActivity {
    TextView phoneChange,passChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_options);
        phoneChange = findViewById(R.id.phone_change);
        passChange = findViewById(R.id.password_change);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        passChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecurityOptions.this,ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        phoneChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecurityOptions.this,ChangePhoneNumber.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SecurityOptions.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
