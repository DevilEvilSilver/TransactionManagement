package com.example.projectnhom7.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.projectnhom7.Model.KhoanDongModel;

import java.util.ArrayList;
import java.util.List;

public class KhoanDongHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "khoanDongListDatabase";
    private static final String KHOANDONG_TABLE = "khoandong";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MONEY = "money";
    private static final String CREATE_GIAODICH_TABLE = "CREATE TABLE " + KHOANDONG_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT, " + MONEY + " INTEGER)";

    private SQLiteDatabase db;

    public KhoanDongHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GIAODICH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KHOANDONG_TABLE);
        onCreate(db);
    }

    public void createDefaultKhoanDongIfNeed()  {
        int count = this.getKhoanDongCount();
        if(count ==0 ) {
            KhoanDongModel khoanDongModel = new KhoanDongModel();
            khoanDongModel.setName("Welcome");
            khoanDongModel.setMoney(0);
            this.insertKhoanDong(khoanDongModel);
        }
    }

    public void insertKhoanDong(KhoanDongModel khoanDong) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, khoanDong.getName());
        cv.put(MONEY, khoanDong.getMoney());
        db.insert(KHOANDONG_TABLE, null, cv);
    }

    public List<KhoanDongModel> getAllKhoanDong() {
        List<KhoanDongModel> khoanDongList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + KHOANDONG_TABLE;
        db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);

        if (cur.moveToFirst()) {
            do {
                KhoanDongModel khoandong = new KhoanDongModel();
                khoandong.setId(cur.getInt(cur.getColumnIndex(ID)));
                khoandong.setName(cur.getString(cur.getColumnIndex(NAME)));
                khoandong.setMoney(cur.getInt(cur.getColumnIndex(MONEY)));
                khoanDongList.add(khoandong);
            } while (cur.moveToNext());
        }

        cur.close();
        return khoanDongList;
    }

    public void updateKhoanDong (int id, String name, int money) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(MONEY, money);
        db.update(KHOANDONG_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteKhoanDong (int id) {
        db = this.getWritableDatabase();
        db.delete(KHOANDONG_TABLE, ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getSumMoney () {
        int sum = 0;
        String selectQuery = "SELECT  * FROM " + KHOANDONG_TABLE;
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

    public int getKhoanDongCount() {
        String countQuery = "SELECT  * FROM " + KHOANDONG_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
}
