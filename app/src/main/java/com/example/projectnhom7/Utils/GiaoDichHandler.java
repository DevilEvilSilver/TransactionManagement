package com.example.projectnhom7.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projectnhom7.Model.GiaoDichModel;
import com.jjoe64.graphview.series.DataPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GiaoDichHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "giaoDichListDatabase";
    private static final String GIAODICH_TABLE = "giaodich";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MONEY = "money";
    private static final String DATE = "date";
    private static final String CREATE_GIAODICH_TABLE = "CREATE TABLE " + GIAODICH_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + NAME + " TEXT, " + MONEY + " INTEGER, " + DATE + " TEXT)";

    private SQLiteDatabase db;

    public GiaoDichHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GIAODICH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GIAODICH_TABLE);
        onCreate(db);
    }

    public void createDefaultGiaoDichIfNeed()  {
        int count = this.getGiaoDichCount();
        if(count ==0 ) {
            GiaoDichModel giaoDichModel = new GiaoDichModel();
            giaoDichModel.setName("Welcome");
            giaoDichModel.setDate("2021/01/01");
            giaoDichModel.setMoney(0);
            this.insertGiaoDich(giaoDichModel);
        }
    }

    public void insertGiaoDich(GiaoDichModel giaodich) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, giaodich.getName());
        cv.put(MONEY, giaodich.getMoney());
        cv.put(DATE, giaodich.getDate());
        db.insert(GIAODICH_TABLE, null, cv);
    }

    public List<GiaoDichModel> getAllGiaoDich() {
        List<GiaoDichModel> giaoDichList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GIAODICH_TABLE + " ORDER BY " + DATE + " ASC";
        db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);

        if (cur.moveToFirst()) {
            do {
                GiaoDichModel giaoDich = new GiaoDichModel();
                giaoDich.setId(cur.getInt(cur.getColumnIndex(ID)));
                giaoDich.setName(cur.getString(cur.getColumnIndex(NAME)));
                giaoDich.setMoney(cur.getInt(cur.getColumnIndex(MONEY)));
                giaoDich.setDate(cur.getString(cur.getColumnIndex(DATE)));
                giaoDichList.add(giaoDich);
            } while (cur.moveToNext());
        }

        cur.close();
        return giaoDichList;
    }

    public void updateGiaoDich (int id, String name, int money, String date) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(MONEY, money);
        cv.put(DATE, date);
        db.update(GIAODICH_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteGiaoDich (int id) {
        db = this.getWritableDatabase();
        db.delete(GIAODICH_TABLE, ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getSumMoney () {
        int sum = 0;
        String selectQuery = "SELECT  * FROM " + GIAODICH_TABLE;
        db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);

        if (cur.moveToFirst()) {
            do {
                sum += cur.getInt(cur.getColumnIndex(MONEY));
            } while (cur.moveToNext());
        }

        cur.close();
        return sum;
    }

    public int getSumMoneyByMonth (int month) throws ParseException {
        int sum = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String selectQuery = "SELECT  * FROM " + GIAODICH_TABLE;
        db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);

        if (cur.moveToFirst()) {
            do {
                Date date = format.parse(cur.getString(cur.getColumnIndex(DATE)));
                cal.setTime(date);
                if (cal.get(Calendar.MONTH) == month)
                    sum += cur.getInt(cur.getColumnIndex(MONEY));
            } while (cur.moveToNext());
        }

        cur.close();
        return sum;
    }

    public int getGiaoDichCount() {
        String countQuery = "SELECT  * FROM " + GIAODICH_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public DataPoint[] grabData() throws ParseException {
        db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + GIAODICH_TABLE + " ORDER BY " + DATE + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataPoint[] dataPoints = new DataPoint[cursor.getCount()];
        int sumToDate = 0;

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date date = format.parse(cursor.getString(cursor.getColumnIndex("date")));
            long milliseconds = date.getTime();
            sumToDate += cursor.getInt(cursor.getColumnIndex("money"));
            dataPoints[i] = new DataPoint(milliseconds, sumToDate);
        }

        cursor.close();
        return dataPoints;
    }
}
