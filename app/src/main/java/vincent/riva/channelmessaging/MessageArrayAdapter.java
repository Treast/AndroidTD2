package vincent.riva.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;
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
        final View rowView = inflater.inflate(R.layout.message_layout, parent, false);
        final TextView textViewUser = (TextView)rowView.findViewById(R.id.textViewUser);
        final TextView textViewDate = (TextView)rowView.findViewById(R.id.textViewDate);
        TextView textViewMessage = (TextView)rowView.findViewById(R.id.textViewMessage);
        final ImageView imageViewAvatar = (ImageView)rowView.findViewById(R.id.imageViewAvatar);

        ResponseUser u = this.users.get(message.getUserID());

        AsyncGetClass asyncGet = new AsyncGetClass();

        asyncGet.setOnCompleteDownloadListener(new OnDownloadListener() {
            @Override
            public void onCompleteDownload(Bitmap b) {
                ImageView imageViewAvatar = (ImageView)rowView.findViewById(R.id.imageViewAvatar);
                imageViewAvatar.setImageBitmap(getRoundedCornerBitmap(b));
            }
        });

        asyncGet.execute(u.getImageUrl());

        textViewUser.setText(u.getIdentifiant());
        textViewDate.setText(message.getData());
        textViewMessage.setText(message.getMessage());
        return rowView;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
