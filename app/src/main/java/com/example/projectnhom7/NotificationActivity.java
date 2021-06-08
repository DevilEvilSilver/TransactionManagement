package com.example.projectnhom7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class NotificationActivity extends AppCompatActivity {

    private Switch switchTK, switchKD;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();

        switchTK = findViewById(R.id.switchTK);
        switchKD = findViewById(R.id.switchKD);

        preferences = getSharedPreferences("Account", Context.MODE_PRIVATE);

        switchTK.setChecked(preferences.getBoolean("switchTK", false));
        switchKD.setChecked(preferences.getBoolean("switchKD", false));

        switchTK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor prefEditor = preferences.edit();
                if (isChecked) {
                    prefEditor.putBoolean("switchTK", true);
                } else {
                    prefEditor.putBoolean("switchTK", false);
                }
                prefEditor.commit();
            }
        });

        switchKD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor prefEditor = preferences.edit();
                if (isChecked) {
                    prefEditor.putBoolean("switchKD", true);
                } else {
                    prefEditor.putBoolean("switchKD", false);
                }
                prefEditor.commit();
            }
        });
    }
}