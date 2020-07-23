package com.kipchulovidovi.lovidovi.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.kipchulovidovi.lovidovi.R;
import com.kipchulovidovi.lovidovi.utils.Constants;
import com.kipchulovidovi.lovidovi.utils.SharedPreferencesConfig;

public class SettingsActivity extends AppCompatActivity {
    ImageView firstLetterImageView;
    TextView usernameTextView, emailTextView;
    private SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());


        firstLetterImageView = findViewById(R.id.first_letter_image_view);
        usernameTextView = findViewById(R.id.username_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        String status = sharedPreferencesConfig.readClientsStatus();

        if(status.contentEquals(Constants.ACTIVE_CONSTANT)) {
            String username = sharedPreferencesConfig.readClientsUsername();
            String location = "LoviDovi";
            usernameTextView.setText(username);
            emailTextView.setText(location);

            getFirstLetterInCircularBackground(firstLetterImageView, username);
        }
        else {
            String username = usernameTextView.getText().toString();
            getFirstLetterInCircularBackground(firstLetterImageView, username);

        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            final Context context = getContext();

            Preference preferenceChangePassword, preferencePersonalInformation;
            preferenceChangePassword = findPreference("security_information");
            preferencePersonalInformation = findPreference("personal_information");

            preferenceChangePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(context,SecurityOptions.class);
                    startActivity(intent);
                    ((Activity)context).finish();

                    return true;
                }
            });

            preferencePersonalInformation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(context, ChangePersonalInfo.class);
                    startActivity(intent);

                    return false;
                }
            });


        }
    }
    public void getFirstLetterInCircularBackground(ImageView imageView, String username){
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
//        generate random color
//        int color = generator.getColor(getItem());

        int color = generator.getRandomColor();
        String firstLetter = String.valueOf(username.charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        imageView.setImageDrawable(drawable);
    }

}
