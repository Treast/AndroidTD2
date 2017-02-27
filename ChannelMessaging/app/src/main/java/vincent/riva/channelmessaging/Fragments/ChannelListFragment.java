package vincent.riva.channelmessaging.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import vincent.riva.channelmessaging.AsyncTaskClass;
import vincent.riva.channelmessaging.ChannelActivity;
import vincent.riva.channelmessaging.ChannelArrayAdapter;
import vincent.riva.channelmessaging.ChannelListActivity;
import vincent.riva.channelmessaging.FriendsActivity;
import vincent.riva.channelmessaging.OnCompleteRequestListener;
import vincent.riva.channelmessaging.R;
import vincent.riva.channelmessaging.ResponseChannel;
import vincent.riva.channelmessaging.ResponseChannelList;


public class ChannelListFragment extends Fragment {

    public List<ResponseChannel> channels;
    private ListView listViewChannels;
    private Button buttonAmis;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel_list, container, false);
        this.listViewChannels = (ListView)v.findViewById(R.id.listViewChannels);
        this.buttonAmis = (Button)v.findViewById(R.id.buttonAmis);
        this.buttonAmis.setOnClickListener((ChannelListActivity)getActivity());

        return v;
    }

    public void findMessages()
    {
        AsyncTaskClass async = new AsyncTaskClass();
        async.setOnCompleteRequestListener((ChannelListActivity)getActivity());
        async.execute("http://www.raphaelbischof.fr/messaging/?function=getchannels", "accesstoken", this.token);
    }

    public void setChannels(Context context, List<ResponseChannel> channels)
    {
        this.channels = channels;
        this.listViewChannels.setAdapter(new ChannelArrayAdapter(context, this.channels));
        this.listViewChannels.setOnItemClickListener((ChannelListActivity)getActivity());
    }

    public void setToken(String token)
    {
        Log.d("Toek", token);
        this.token = token;
    }
}
