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
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectnhom7.Utils.GiaoDichHandler;
import com.example.projectnhom7.Utils.KhoanDongHandler;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AccountActivity extends AppCompatActivity {
    private TextView username, sum;
    private EditText newUsername, oldPwd, newPwd, reNewPwd;
    private Button updateUsername, updatePwd;
    private GiaoDichHandler db;
    private KhoanDongHandler dbKD;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        username = findViewById(R.id.TenTaikhoanView);
        sum = findViewById(R.id.SoDuView);
        newUsername = findViewById(R.id.TenTaiKhoanInput);
        oldPwd = findViewById(R.id.MatKhauCuInput);
        newPwd = findViewById(R.id.MatKhauMoiInput);
        reNewPwd = findViewById(R.id.MatKhauMoiLaiInput);
        updateUsername = findViewById(R.id.buttonTenTaiKhoan);
        updatePwd = findViewById(R.id.buttonMatKhau);

        db = new GiaoDichHandler(this);
        db.createDefaultGiaoDichIfNeed();
        dbKD = new KhoanDongHandler(this);
        dbKD.createDefaultKhoanDongIfNeed();

        preferences = getSharedPreferences("Account", Context.MODE_PRIVATE);

        username.setText(preferences.getString("id", "user"));
        sum.setText(String.valueOf(db.getSumMoney()));
        updateUsername.setEnabled(false);
        updatePwd.setEnabled(false);

        if (preferences.getBoolean("switchTK", false)) {
            Date curr = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);

            try {
                if (db.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                    Toast.makeText(AccountActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (preferences.getBoolean("switchKD", false)) {
            if (db.getSumMoney() < dbKD.getSumMoney())
                Toast.makeText(AccountActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
        }

        newUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    updateUsername.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    updateUsername.setEnabled(false);
                }
                else{
                    updateUsername.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        oldPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
                else{
                    updatePwd.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
                else{
                    updatePwd.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        reNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    updatePwd.setEnabled(false);
                }
                else{
                    updatePwd.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString("id", newUsername.getText().toString());
                prefEditor.commit();
                username.setText(preferences.getString("id", "user"));
                Toast.makeText(AccountActivity.this, "đổi tên tài khoản thành công", Toast.LENGTH_SHORT).show();
                newUsername.setText("");
            }
        });

        updatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("passcode", "1234").equals(oldPwd.getText().toString())
                        && newPwd.getText().toString().equals(reNewPwd.getText().toString())) {
                    SharedPreferences.Editor prefEditor = preferences.edit();
                    prefEditor.putString("passcode", newPwd.getText().toString());
                    prefEditor.commit();
                    Toast.makeText(AccountActivity.this, "đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    oldPwd.setText("");
                    newPwd.setText("");
                    reNewPwd.setText("");
                }
                else {
                    Toast.makeText(AccountActivity.this, "thông tin không hợp lệ", Toast.LENGTH_SHORT).show();
                    oldPwd.setText("");
                    newPwd.setText("");
                    reNewPwd.setText("");
                }
            }
        });
    }
}