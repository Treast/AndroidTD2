package riva.vincent.channelmessaging.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import riva.vincent.channelmessaging.async.AsyncTaskClass;
import riva.vincent.channelmessaging.channel.ChannelArrayAdapter;
import riva.vincent.channelmessaging.channel.ChannelListActivity;
import riva.vincent.channelmessaging.R;
import riva.vincent.channelmessaging.response.ResponseChannel;


public class ChannelListFragment extends Fragment {

    public List<ResponseChannel> channels;
    private ListView listViewChannels;
    private Button buttonAmis;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel_list, container, false);


        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
        this.token = settings.getString("accesstoken", "");

        this.listViewChannels = (ListView)v.findViewById(R.id.listViewChannels);
        this.buttonAmis = (Button)v.findViewById(R.id.buttonAmis);
        this.buttonAmis.setOnClickListener((ChannelListActivity)getActivity());
        this.findMessages();
        return v;
    }

    public void findMessages()
    {
        AsyncTaskClass async = new AsyncTaskClass();
        async.setOnCompleteRequestListener((ChannelListActivity)getActivity());
        async.execute("http://www.raphaelbischof.fr/messaging/?function=getchannels", "accesstoken", this.token);
    }

    public void setChannels(List<ResponseChannel> channels)
    {
        this.channels = channels;
        if(!this.channels.isEmpty())
        {
            this.listViewChannels.setAdapter(new ChannelArrayAdapter(getActivity().getApplicationContext(), this.channels));
            this.listViewChannels.setOnItemClickListener((ChannelListActivity)getActivity());
        }
    }
}
