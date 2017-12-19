package com.example.ray.sqlite_ex;

import com.example.ray.sqlite_ex.BookContract.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/*
此範例展示如何使用正權的原則使用Android SQLite
1. 建立Contract: 將所須要的版本, 資料庫名稱, 資料表名稱, 欄位名稱建立成一個class. 且此class不允許產生實體, 僅提供一些資料結構資訊.
2. 建立SQLiteOpenHelper子類別管理資料庫的建立, 升級, 降級. 他提供getWritableDatabase/getReadableDatabase來讓主程式或取資料庫.
3. 利用getWritableDatabase/getReadableDatabase獲取資料庫操作, 執行操作.

note: getWritableDatabase/getReadableDatabase有很大的overhead, 通常只在activity發生onDestroy時才關閉.
 */

public class MainActivity extends AppCompatActivity{

    ListView booksListView;
    SimpleCursorAdapter simpleCursorAdapter;
    SQLiteDatabase db;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    booksListView.setAdapter(simpleCursorAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksListView = findViewById(R.id.lstView_books);

        //資料庫的操作費時, 必須在其他thread中完成.
        new Thread(new Runnable() {
            @Override
            public void run() {
                BookDBHelper bookDBHelper = BookDBHelper.getInstance(MainActivity.this);
                db = bookDBHelper.getWritableDatabase();

                //利用ContentValues插入資料
                ContentValues contentValues = new ContentValues();
                contentValues.put(BookEntry.TITLE, "How to be a billionaire!");
                contentValues.put(BookEntry.AUTHOR, "Ray");
                db.insert(BookEntry.TABLE_NAME, null, contentValues);
                contentValues.put(BookEntry.TITLE, "Good Job");
                contentValues.put(BookEntry.AUTHOR, "Donald Trump");
                db.insert(BookEntry.TABLE_NAME, null, contentValues);

                //讀取資料
                Cursor c = db.query(BookEntry.TABLE_NAME, null, null, null, null, null, null);
                simpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_2, c, new String[]{BookEntry.TITLE, BookEntry.AUTHOR}, new int[]{android.R.id.text1, android.R.id.text2}, 0);

                //通知handler更新資料
                handler.sendEmptyMessage(0x01);
            }
        }).start();
    }

    //通常在整個activity結束時才關閉資料庫, 因為取得資料庫(getWritableDatabase/getReadableDatabase)會有很大的overhead
    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
