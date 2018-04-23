package weekl.chatdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChatDemoDatabase.db";
    private static int DB_VERSION = 1;

    private static String mUser;
    private static DbHelper mInstance;
    private SQLiteDatabase mDb;

    private final String TABLE_CONVERSATION = mUser + "Conversation";
    private final String TABLE_RECORD = mUser + "MsgRecord";

    private final String CREATE_CONVERSATION_DB = "create table if not exists " + TABLE_CONVERSATION + "(" +
            "_id integer primary key autoincrement," +
            "target text," +
            "time text," +
            "content text," +
            "unread integer)";

    public static DbHelper getInstance(String user, Context context){
        mUser = user;
        if (mInstance == null){
            synchronized(DbHelper.class){
                if (mInstance == null){
                    mInstance = new DbHelper(context,DB_NAME,null,DB_VERSION);
                }
            }
        }
        return mInstance;
    }

    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
