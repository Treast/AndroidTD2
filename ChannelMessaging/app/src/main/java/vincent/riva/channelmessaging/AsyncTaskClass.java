package vincent.riva.channelmessaging;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by rivav on 20/01/2017.
 */
public class AsyncTaskClass extends AsyncTask<String, Integer, String> {

    ArrayList<OnCompleteRequestListener> onCompleteListeners = new ArrayList<>();

    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close()
            ;
            os.close()
            ;
            int responseCode
                    = conn.getResponseCode();
            if
                    (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br
                        = new BufferedReader
                        (new InputStreamReader
                                (conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line
                    ;
                }
            } else {
                response
                        = "";
            }
        } catch (Exception e) {
            e.printStackTrace()
            ;
        }
        return response
                ;
    }

    private String getPostDataString (HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for
                (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String, String> hash = new HashMap<>();
        for(int i = 1; i<params.length; i=i+2)
            hash.put(params[i], params[i+1]);
        return performPostCall(params[0], hash);
    }

    @Override
    protected void onPostExecute(String response) {
        for(OnCompleteRequestListener listener : this.onCompleteListeners)
            listener.onCompleteRequest(response);
    }

    public void setOnCompleteRequestListener(OnCompleteRequestListener listener)
    {
        this.onCompleteListeners.add(listener);
    }
}
