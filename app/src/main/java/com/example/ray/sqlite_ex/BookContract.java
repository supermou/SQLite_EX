package com.example.ray.sqlite_ex;

import android.provider.BaseColumns;

/**
 * Created by Ray on 2017/12/19.
 */

//一個書的資料庫, 提供給資料庫的一個結構.
public class BookContract {
    //此class不允許其他人建立實體, 設定constructor為private
    private BookContract(){}

    //建立外部可存取的條目
    //implement BaseColumns就會先包含_id, _count
    public static class BookEntry implements BaseColumns{
        public final static int VERSION = 1;
        public final static String DB_NAME = "book.db";
        public final static String TABLE_NAME = "books";
        public final static String TITLE = "title";
        public final static String AUTHOR = "author";
    }
}
