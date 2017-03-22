package riva.vincent.channelmessaging.channel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;

import riva.vincent.channelmessaging.ChannelActivity;
import riva.vincent.channelmessaging.friends.FriendsActivity;
import riva.vincent.channelmessaging.GPSActivity;
import riva.vincent.channelmessaging.listeners.OnCompleteRequestListener;
import riva.vincent.channelmessaging.R;
import riva.vincent.channelmessaging.response.ResponseChannel;
import riva.vincent.channelmessaging.response.ResponseChannelList;
import riva.vincent.channelmessaging.fragments.ChannelListFragment;
import riva.vincent.channelmessaging.fragments.MessageFragment;

public class ChannelListActivity extends GPSActivity implements View.OnClickListener, OnCompleteRequestListener, AdapterView.OnItemClickListener{

    private ResponseChannelList channelList;
    private String token;
    private int channelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
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
        fragment.setChannels(channelList.getChannels());
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
            fragment.setChannelID(channel.getChannelID());
        }
    }


}
