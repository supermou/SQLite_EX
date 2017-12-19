package com.example.ray.sqlite_ex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ray.sqlite_ex.BookContract.*;

/**
 * Created by Ray on 2017/12/19.
 */

//建立DB Helper, 處理建立database與database upgrade等.
//使用singleton方式, 只允許產生一個實體.
public class BookDBHelper extends SQLiteOpenHelper {

    private static BookDBHelper instance;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                    + BookEntry._ID + " INTEGER PRIMARY KEY, "
                    + BookEntry.TITLE + " TEXT, "
                    + BookEntry.AUTHOR + " TEXT)";

    private static final String SQL_DEL_TABLE =
            "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;

    //更改為private, 不讓外處產生instance. 使用singleton模式.
    private BookDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //callback function當資料庫或資料表不存在時執行
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    //callback function當version不同時執行, 可操作資料表的欄位.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //此例只是當版本不同時, 重新建立資料表.
        sqLiteDatabase.execSQL(SQL_DEL_TABLE);
        onCreate(sqLiteDatabase);
    }

    //建立外部獲取instance function.(singleton 模式)
    public static BookDBHelper getInstance(Context context) {
        if (instance == null) instance = new BookDBHelper(context, BookEntry.DB_NAME, null, BookEntry.VERSION);
        return instance;
    }
}
