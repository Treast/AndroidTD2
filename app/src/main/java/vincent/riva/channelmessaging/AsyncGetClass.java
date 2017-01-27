package vincent.riva.channelmessaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rivav on 23/01/2017.
 */
public class AsyncGetClass extends AsyncTask<String, Void, Bitmap>{
    ArrayList<OnDownloadListener> onCompleteDownloads = new ArrayList<>();

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            //TODO: Handle error
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap b) {
        for(OnDownloadListener listener : this.onCompleteDownloads)
            listener.onCompleteDownload(b);
    }

    protected void setOnCompleteDownloadListener(OnDownloadListener listener)
    {
        this.onCompleteDownloads.add(listener);
    }
}
