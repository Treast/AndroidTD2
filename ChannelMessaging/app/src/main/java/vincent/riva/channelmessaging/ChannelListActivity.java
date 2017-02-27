package vincent.riva.channelmessaging;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vincent.riva.channelmessaging.Fragments.ChannelListFragment;
import vincent.riva.channelmessaging.Fragments.MessageFragment;

public class ChannelListActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteRequestListener, AdapterView.OnItemClickListener{

    private ResponseChannelList channelList;
    private String token;
    private int channelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        this.token = settings.getString("accesstoken", "");

        ChannelListFragment fragment = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentChannelList);
        fragment.setToken(this.token);
        fragment.findMessages();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonAmis:
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCompleteRequest(String response) {
        Gson gson = new Gson();
        channelList = gson.fromJson(response, ResponseChannelList.class);
        ChannelListFragment fragment = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentChannelList);
        fragment.setChannels(getApplicationContext(), channelList.getChannels());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ResponseChannel channel = channelList.getChannels().get(position);
        this.channelID = channel.getChannelID();
        MessageFragment fragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMessage);

        if(fragment == null|| !fragment.isInLayout()){
            Intent i = new Intent(getApplicationContext(), ChannelActivity.class);
            i.putExtra("channelID", channel.getChannelID());
            startActivity(i);
        } else {
            fragment.setUp(this.token, channel.getChannelID());
        }
    }

    public void refreshMessages()
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", token, "channelid", channelID+"");
    }
}
