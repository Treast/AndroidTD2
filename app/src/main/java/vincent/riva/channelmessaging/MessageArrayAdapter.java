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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter<ResponseMessage> {

    private final Context context;
    private ResponseUserList users;

    public MessageArrayAdapter(Context context, List<ResponseMessage> objects, ResponseUserList users) {
        super(context, R.layout.message_layout, objects);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseMessage message = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message_layout, parent, false);
        TextView textViewUser = (TextView)rowView.findViewById(R.id.textViewUser);
        TextView textViewDate = (TextView)rowView.findViewById(R.id.textViewDate);
        TextView textViewMessage = (TextView)rowView.findViewById(R.id.textViewMessage);
        ImageView imageViewAvatar = (ImageView)rowView.findViewById(R.id.imageViewAvatar);

        ResponseUser u = this.users.get(message.getUserID());

        textViewUser.setText(u.getIdentifiant());
        textViewDate.setText(message.getData());
        textViewMessage.setText(message.getMessage());
        return rowView;
    }
}
