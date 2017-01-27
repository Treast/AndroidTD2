package vincent.riva.channelmessaging;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;

public class ChannelActivity extends Activity {

    private String accesstoken;
    private int channelID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        this.context = this;
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
        final AsyncTaskClass async = new AsyncTaskClass();

        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                final ListView listViewMessages = (ListView)findViewById(R.id.listViewMessages);
                Gson gson = new Gson();
                ResponseMessageList messageList = gson.fromJson(response, ResponseMessageList.class);
                listViewMessages.setAdapter(new MessageArrayAdapter(getApplicationContext(), messageList.getMessages()));
                listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final ResponseMessage message = (ResponseMessage)listViewMessages.getItemAtPosition(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Voulez vous vraiment ajotuer cet utilisateur à votre liste d'ami ?").setTitle("Ajouter un ami");
                        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                UserDataSource userDataSource = new UserDataSource(getApplicationContext());
                                userDataSource.open();
                                userDataSource.createFriend(message.getUsername(), message.getImageUrl());
                                userDataSource.close();
                            }
                        });
                        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
        async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", accesstoken, "channelid", channelID+"");
    }
}
