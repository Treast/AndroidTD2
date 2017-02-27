package vincent.riva.channelmessaging;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rivav on 27/01/2017.
 */
public class FriendsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FriendsDB22.db";
    public static final String FRIEND_TABLE_NAME = "Friend";
    public static final String KEY_ID = "userID";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGEURL = "imageUrl";
    private static final String FRIEND_TABLE_CREATE = "CREATE TABLE "+FRIEND_TABLE_NAME+" ("+KEY_ID+" TEXT, "+KEY_USERNAME+" TEXT, "+KEY_IMAGEURL+" TEXT);";

    public FriendsDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FRIEND_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+FRIEND_TABLE_NAME);
        onCreate(db);
    }
}
