package com.example.projectnhom7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectnhom7.Adapter.GiaoDichAdapter;
import com.example.projectnhom7.Adapter.KhoanDongAdapter;
import com.example.projectnhom7.Model.GiaoDichModel;
import com.example.projectnhom7.Model.KhoanDongModel;
import com.example.projectnhom7.Utils.GiaoDichHandler;
import com.example.projectnhom7.Utils.KhoanDongHandler;
import com.example.projectnhom7.Utils.RecycleGDTouchHelper;
import com.example.projectnhom7.Utils.RecycleKDTouchHelper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SavingActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView khoanDongRecycleView;
    private KhoanDongAdapter khoanDongAdapter;
    private List<KhoanDongModel> khoanDongList;
    private KhoanDongHandler db;
    private GiaoDichHandler dbGD;

    private EditText savingInput;
    private EditText khoanDongName;
    private EditText khoanDongMoney;
    private Button addKhoanDongButton, savingButton;

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
        getSupportActionBar().hide();

        savingInput = findViewById(R.id.TietKiemInput);
        khoanDongName = Objects.requireNonNull(findViewById(R.id.KhoanDongNameInput));
        khoanDongMoney = Objects.requireNonNull(findViewById(R.id.KhoanDongMoneyInput));
        savingButton = findViewById(R.id.buttonTietKiem);
        addKhoanDongButton = findViewById(R.id.buttonThemKD);

        preferences = getSharedPreferences("Account", Context.MODE_PRIVATE);

        savingInput.setText(String.valueOf(preferences.getInt("saving", 0)));

        db = new KhoanDongHandler(this);
        db.createDefaultKhoanDongIfNeed();
        dbGD = new GiaoDichHandler(this);
        dbGD.createDefaultGiaoDichIfNeed();

        khoanDongRecycleView = findViewById(R.id.khoanDongRecycleView);
        khoanDongRecycleView.setLayoutManager(new LinearLayoutManager(this));
        khoanDongAdapter = new KhoanDongAdapter(db, this);
        khoanDongRecycleView.setAdapter(khoanDongAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecycleKDTouchHelper(khoanDongAdapter));
        itemTouchHelper.attachToRecyclerView(khoanDongRecycleView);

        khoanDongList = db.getAllKhoanDong();
        Collections.reverse(khoanDongList);

        khoanDongAdapter.setKhoanDong(khoanDongList);

        savingButton.setEnabled(false);
        addKhoanDongButton.setEnabled(false);

        if (preferences.getBoolean("switchTK", false)) {
            Date curr = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);

            try {
                if (dbGD.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                    Toast.makeText(SavingActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (preferences.getBoolean("switchKD", false)) {
            if (dbGD.getSumMoney() < db.getSumMoney())
                Toast.makeText(SavingActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
        }

        savingInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    savingButton.setEnabled(false);
                }
                else{
                    savingButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        khoanDongName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    addKhoanDongButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    addKhoanDongButton.setEnabled(false);
                }
                else{
                    addKhoanDongButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        khoanDongMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    addKhoanDongButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    addKhoanDongButton.setEnabled(false);
                }
                else{
                    addKhoanDongButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        savingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int saving = Integer.parseInt(savingInput.getText().toString());
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putInt("saving", saving);
                prefEditor.commit();

                savingInput.setText(String.valueOf(preferences.getInt("saving", 0)));
                savingButton.setEnabled(false);

                if (preferences.getBoolean("switchTK", false)) {
                    Date curr = Calendar.getInstance().getTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curr);

                    try {
                        if (dbGD.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                            Toast.makeText(SavingActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addKhoanDongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = khoanDongName.getText().toString();
                int money = Integer.parseInt(khoanDongMoney.getText().toString());

                KhoanDongModel khoanDong = new KhoanDongModel();
                khoanDong.setName(name);
                khoanDong.setMoney(money);
                db.insertKhoanDong(khoanDong);

                khoanDongName.setText("");
                khoanDongMoney.setText("");

                khoanDongList.clear();
                khoanDongList = db.getAllKhoanDong();
                Collections.reverse(khoanDongList);
                khoanDongAdapter.setKhoanDong(khoanDongList);

                if (preferences.getBoolean("switchKD", false)) {
                    if (dbGD.getSumMoney() < db.getSumMoney())
                        Toast.makeText(SavingActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        if (preferences.getBoolean("switchKD", false)) {
            if (dbGD.getSumMoney() < db.getSumMoney())
                Toast.makeText(SavingActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
        }

        khoanDongList = db.getAllKhoanDong();
        Collections.reverse(khoanDongList);
        khoanDongAdapter.setKhoanDong(khoanDongList);
        khoanDongAdapter.notifyDataSetChanged();
    }

}