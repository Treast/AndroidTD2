package vincent.riva.channelmessaging;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

public class ChannelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        int channelID = getIntent().getIntExtra("channelID", 1);
        final ListView listViewMessages = (ListView)findViewById(R.id.listViewMessages);

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        String token = settings.getString("accesstoken", "");

        AsyncTaskClass async = new AsyncTaskClass();

        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                Gson gson = new Gson();
                ResponseMessageList messageList = gson.fromJson(response, ResponseMessageList.class);
                listViewMessages.setAdapter(new MessageArrayAdapter(getApplicationContext(), messageList.getMessages()));
            }
        });

        async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", token, "channelid", channelID+"");
    }
}
