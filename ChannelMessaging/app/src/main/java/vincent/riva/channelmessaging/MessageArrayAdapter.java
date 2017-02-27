package vincent.riva.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter<ResponseMessage> {

    private final Context context;

    public MessageArrayAdapter(Context context, List<ResponseMessage> objects) {
        super(context, R.layout.message_layout, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseMessage message = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.message_layout, parent, false);
        final TextView textViewUser = (TextView)rowView.findViewById(R.id.textViewUser);
        final TextView textViewDate = (TextView)rowView.findViewById(R.id.textViewDate);
        final TextView textViewMessage = (TextView)rowView.findViewById(R.id.textViewMessage);
        final ImageView imageViewAvatar = (ImageView)rowView.findViewById(R.id.imageViewAvatar);
        final ImageView imageViewImage = (ImageView)rowView.findViewById(R.id.imageViewImage);

        //ResponseUser u = this.users.get(message.getUserID());

        if(message.getImageUrl() != null)
        {
            AsyncGetClass async = new AsyncGetClass(getContext());

            async.setOnCompleteDownloadListener(new OnDownloadListener() {
                @Override
                public void onCompleteDownload(String r) {
                    File f = new File(getContext().getFilesDir(), r);
                    Bitmap image = BitmapFactory.decodeFile(f.getAbsolutePath());
                    Bitmap avatar = getRoundedCornerBitmap(MessageArrayAdapter.getRoundedCornerBitmap(image));
                    imageViewAvatar.setImageBitmap(avatar);
                }
            });

            async.execute(message.getUsername()+".bmp", message.getImageUrl());
        }

        if(message.getMessageImageUrl() != "")
        {
            AsyncGetClass async = new AsyncGetClass(getContext());

            async.setOnCompleteDownloadListener(new OnDownloadListener() {
                @Override
                public void onCompleteDownload(String r) {
                    File f = new File(getContext().getFilesDir(), r);
                    Bitmap image = BitmapFactory.decodeFile(f.getAbsolutePath());
                    textViewMessage.setVisibility(View.INVISIBLE);
                    imageViewImage.setImageBitmap(image);
                }
            });
            String filename = message.getMessageImageUrl().substring(message.getMessageImageUrl().lastIndexOf('/')+1,message.getMessageImageUrl().length() );
            async.execute(filename, message.getMessageImageUrl());
        } else {
            imageViewImage.setVisibility(View.INVISIBLE);
        }

        textViewUser.setText(message.getUsername());
        textViewDate.setText(message.getDate());
        textViewMessage.setText(message.getMessage());
        return rowView;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
