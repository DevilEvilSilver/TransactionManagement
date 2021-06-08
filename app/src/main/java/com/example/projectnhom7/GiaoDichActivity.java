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
import com.example.projectnhom7.Model.GiaoDichModel;
import com.example.projectnhom7.Utils.GiaoDichHandler;
import com.example.projectnhom7.Utils.KhoanDongHandler;
import com.example.projectnhom7.Utils.RecycleGDTouchHelper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GiaoDichActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView giaoDichRecycleView;
    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDichModel> giaoDichList;
    private GiaoDichHandler db;
    private KhoanDongHandler dbKD;

    private EditText giaoDichName;
    private EditText giaoDichMoney;
    private EditText giaoDichDate;
    private DatePickerDialog picker;
    private Button addGiaoDichButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich);
        getSupportActionBar().hide();

        giaoDichName = Objects.requireNonNull(findViewById(R.id.GiaoDichNameInput));
        giaoDichMoney = Objects.requireNonNull(findViewById(R.id.GiaoDichMoneyInput));
        giaoDichDate = Objects.requireNonNull(findViewById(R.id.GiaoDichDateInput));
        addGiaoDichButton = findViewById(R.id.buttonThemGD);

        preferences = getSharedPreferences("Account", Context.MODE_PRIVATE);

        db = new GiaoDichHandler(this);
        db.createDefaultGiaoDichIfNeed();
        KhoanDongHandler dbKD = new KhoanDongHandler(this);
        dbKD.createDefaultKhoanDongIfNeed();

        giaoDichRecycleView = findViewById(R.id.giaoDichRecycleView);
        giaoDichRecycleView.setLayoutManager(new LinearLayoutManager(this));
        giaoDichAdapter = new GiaoDichAdapter(db, this);
        giaoDichRecycleView.setAdapter(giaoDichAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecycleGDTouchHelper(giaoDichAdapter));
        itemTouchHelper.attachToRecyclerView(giaoDichRecycleView);

        giaoDichList = db.getAllGiaoDich();
        Collections.reverse(giaoDichList);

        giaoDichAdapter.setGiaoDich(giaoDichList);

        if (preferences.getBoolean("switchTK", false)) {
            Date curr = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);

            try {
                if (db.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                Toast.makeText(GiaoDichActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (preferences.getBoolean("switchKD", false)) {
            if (db.getSumMoney() < dbKD.getSumMoney())
                Toast.makeText(GiaoDichActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
        }

        giaoDichName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    addGiaoDichButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    addGiaoDichButton.setEnabled(false);
                }
                else{
                    addGiaoDichButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        giaoDichMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    addGiaoDichButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    addGiaoDichButton.setEnabled(false);
                }
                else{
                    addGiaoDichButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        giaoDichDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(GiaoDichActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                giaoDichDate.setText(year + "/" + (monthOfYear + 1 < 10 ? "0" : "") + (monthOfYear + 1) + "/" + (dayOfMonth < 10 ? "0" : "") + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        addGiaoDichButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = giaoDichName.getText().toString();
                int money = Integer.parseInt(giaoDichMoney.getText().toString());
                String date = giaoDichDate.getText().toString();

                GiaoDichModel giaoDich = new GiaoDichModel();
                giaoDich.setName(name);
                giaoDich.setMoney(money);
                giaoDich.setDate(date);
                db.insertGiaoDich(giaoDich);

                giaoDichName.setText("");
                giaoDichMoney.setText("");
                giaoDichDate.setText("");

                giaoDichList.clear();
                giaoDichList = db.getAllGiaoDich();
                Collections.reverse(giaoDichList);
                giaoDichAdapter.setGiaoDich(giaoDichList);

                if (preferences.getBoolean("switchTK", false)) {
                    Date curr = Calendar.getInstance().getTime();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curr);

                    try {
                        if (db.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                            Toast.makeText(GiaoDichActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (preferences.getBoolean("switchKD", false)) {
                    if (db.getSumMoney() < dbKD.getSumMoney())
                        Toast.makeText(GiaoDichActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        if (preferences.getBoolean("switchTK", false)) {
            Date curr = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);

            try {
                if (db.getSumMoneyByMonth(cal.get(Calendar.MONTH)) < preferences.getInt("saving", 0))
                    Toast.makeText(GiaoDichActivity.this, "TỔNG TIỀN TRONG THÁNG HIỆN ĐANG THẤP HƠN MỤC TIÊU TIẾT KIỆM", Toast.LENGTH_LONG).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (preferences.getBoolean("switchKD", false)) {
            if (db.getSumMoney() < dbKD.getSumMoney())
                Toast.makeText(GiaoDichActivity.this, "SỐ DƯ CỦA HIỆN TẠI KHÔNG ĐỦ CHO CÁC KHOẢN CẦN ĐÓNG", Toast.LENGTH_LONG).show();
        }

        giaoDichList = db.getAllGiaoDich();
        Collections.reverse(giaoDichList);
        giaoDichAdapter.setGiaoDich(giaoDichList);
        giaoDichAdapter.notifyDataSetChanged();
    }

}