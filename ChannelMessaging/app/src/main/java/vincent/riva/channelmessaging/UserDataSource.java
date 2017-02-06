package vincent.riva.channelmessaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rivav on 27/01/2017.
 */
public class UserDataSource {
    private SQLiteDatabase database;
    private FriendsDB dbHelper;
    private String[] allColumns = {FriendsDB.KEY_ID, FriendsDB.KEY_USERNAME, FriendsDB.KEY_IMAGEURL};

    public UserDataSource(Context context)
    {
        dbHelper = new FriendsDB(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }
    public void close()
    {
        dbHelper.close();
    }

    public Friend createFriend(String username, String imageUrl)
    {
        ContentValues values = new ContentValues();
        values.put(FriendsDB.KEY_USERNAME, username);
        values.put(FriendsDB.KEY_IMAGEURL, imageUrl);
        UUID newID = UUID.randomUUID();
        values.put(FriendsDB.KEY_ID, newID.toString());

        database.insert(FriendsDB.FRIEND_TABLE_NAME, null, values);

        Cursor cursor = database.query(FriendsDB.FRIEND_TABLE_NAME, allColumns, FriendsDB.KEY_ID+"=\'"+newID.toString()+"\'", null, null, null, null);
        cursor.moveToFirst();
        Friend friend = cursorToFriend(cursor);
        cursor.close();
        return friend;
    }

    private Friend cursorToFriend(Cursor cursor)
    {
        Friend friend = new Friend();
        String result = cursor.getString(0);
        friend.setUserID(UUID.fromString(result));
        friend.setUsername(cursor.getString(1));
        friend.setImageUrl(cursor.getString(2));
        return friend;
    }

    public List<Friend> getAllFriends()
    {
        List<Friend> friends = new ArrayList<Friend>();
        Cursor cursor  = database.query(FriendsDB.FRIEND_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Friend friend = cursorToFriend(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }
        cursor.close();
        return friends;
    }

    public void deleteFriend(Friend friend)
    {
        UUID id = friend.getUserID();
        database.delete(FriendsDB.FRIEND_TABLE_NAME, FriendsDB.KEY_ID+"=\'"+id+"\'", null);
    }
}
