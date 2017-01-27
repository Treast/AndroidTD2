package vincent.riva.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by rivav on 23/01/2017.
 */
public class AsyncGetClass extends AsyncTask<String, Void, String>{
    ArrayList<OnDownloadListener> onCompleteDownloads = new ArrayList<>();
    private Context context;

    public AsyncGetClass(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        File file = new File(context.getFilesDir(), params[0]);
        if (!file.exists()) {
            try {
                URL url = new URL(params[1]);
                file.createNewFile();
                URLConnection ucon = url.openConnection();
                InputStream is = ucon.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                final int BUFFER_SIZE = 23 * 1024;
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                byte[] baf = new byte[BUFFER_SIZE];
                int actual = 0;
                while (actual != -1) {
                    fos.write(baf, 0, actual);
                    actual = bis.read(baf, 0, BUFFER_SIZE);
                }
                fos.close();
                return params[0];
            } catch (IOException e) {
                return params[0];
            }
        } else {
            return params[0];
        }
    }

    @Override
    protected void onPostExecute(String r) {
        for(OnDownloadListener listener : this.onCompleteDownloads)
            listener.onCompleteDownload(r);
    }

    protected void setOnCompleteDownloadListener(OnDownloadListener listener)
    {
        this.onCompleteDownloads.add(listener);
    }
}
