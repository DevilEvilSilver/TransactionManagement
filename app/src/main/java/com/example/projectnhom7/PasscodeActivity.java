package com.example.projectnhom7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class PasscodeActivity extends AppCompatActivity {
    private EditText accountInput, passcodeInput;
    private Button loginButton;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        getSupportActionBar().hide();

        accountInput = Objects.requireNonNull(findViewById(R.id.AccountInput));
        passcodeInput = Objects.requireNonNull(findViewById(R.id.PasscodeInput));
        loginButton = findViewById(R.id.LoginButton);

        preferences = getSharedPreferences("Account", Context.MODE_PRIVATE);

        accountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    loginButton.setEnabled(false);
                }
                else{
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passcodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    loginButton.setEnabled(false);
                }
                else{
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("passcode", "1234").equals(passcodeInput.getText().toString())
                    && preferences.getString("id", "user").equals(accountInput.getText().toString())) {
                    startActivity(new Intent(PasscodeActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(PasscodeActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                    passcodeInput.setText("");
                }
            }
        });
    }
}