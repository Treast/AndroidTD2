package vincent.riva.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by rivav on 27/01/2017.
 */
public class FriendArrayAdapter extends ArrayAdapter<Friend>{
    private final Context context;

    public FriendArrayAdapter(Context context, List<Friend> objects) {
        super(context, R.layout.friend_layout, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.friend_layout, parent, false);
        final TextView textViewUser = (TextView)rowView.findViewById(R.id.textViewFriendUsername);
        final ImageView imageViewAvatar = (ImageView)rowView.findViewById(R.id.imageViewFriendImage);

        File file = new File(context.getFilesDir(), friend.getUsername()+".bmp");
        if(file.exists()) {
            Bitmap avatar = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageViewAvatar.setImageBitmap(avatar);
        }
        textViewUser.setText(friend.getUsername());
        return rowView;
    }
}
