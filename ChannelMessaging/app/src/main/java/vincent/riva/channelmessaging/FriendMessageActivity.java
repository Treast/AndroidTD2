package vincent.riva.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by rivav on 06/02/2017.
 */
public class FriendMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Button buttonEnvoyer = (Button)findViewById(R.id.buttonEnvoyer);
        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMessage);

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        final String token = settings.getString("accesstoken", "");

        buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async2 = new AsyncTaskClass();
                String message = editTextMessage.getText().toString();
                editTextMessage.setText("");
                async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", token, "userid", getIntent().getIntExtra("userid", 1)+"", "message", message);
            }
        });

        AsyncTaskClass async = new AsyncTaskClass();

        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                Gson gson = new Gson();
                final ResponseMessageList channel = gson.fromJson(response, ResponseMessageList.class);
                ListView listViewMessages = (ListView)findViewById(R.id.listViewMessages);
                listViewMessages.setAdapter(new MessageArrayAdapter(getApplicationContext(), channel.getMessages()));
            }
        });

        async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", token, "userid", getIntent().getIntExtra("userid", 1)+"");
    }
}
