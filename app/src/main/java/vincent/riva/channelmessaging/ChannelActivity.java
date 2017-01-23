package vincent.riva.channelmessaging;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

public class ChannelActivity extends AppCompatActivity {

    private String accesstoken;
    private int channelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        this.channelID = getIntent().getIntExtra("channelID", 1);

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        this.accesstoken = settings.getString("accesstoken", "");

        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        Button buttonEnvoyer = (Button)findViewById(R.id.buttonEnvoyer);

        buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async2 = new AsyncTaskClass();
                String message = editTextMessage.getText().toString();
                editTextMessage.setText("");
                async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", accesstoken, "channelid", channelID+"", "message", message);
            }
        });
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                refreshMessages();
                handler.postDelayed(this, 5000);
            }
        };
        r.run();
    }

    public void getUsers()
    {
    }

    private void refreshMessages()
    {
        AsyncTaskClass asyncUsers = new AsyncTaskClass();

        asyncUsers.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                Gson gson = new Gson();
                final ResponseUserList usersList = gson.fromJson(response, ResponseUserList.class);

                final AsyncTaskClass async = new AsyncTaskClass();

                async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
                    @Override
                    public void onCompleteRequest(String response) {
                        final ListView listViewMessages = (ListView)findViewById(R.id.listViewMessages);
                        Gson gson = new Gson();
                        ResponseMessageList messageList = gson.fromJson(response, ResponseMessageList.class);
                        listViewMessages.setAdapter(new MessageArrayAdapter(getApplicationContext(), messageList.getMessages(), usersList));
                    }
                });
                async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", accesstoken, "channelid", channelID+"");
            }
        });

        asyncUsers.execute("http://www.raphaelbischof.fr/messaging/?function=getchannelusers", "accesstoken", this.accesstoken, "channelid", this.channelID+"");
    }
}
