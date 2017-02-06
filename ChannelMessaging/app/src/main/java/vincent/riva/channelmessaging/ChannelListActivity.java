package vincent.riva.channelmessaging;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChannelListActivity extends Activity {

    private ResponseChannelList channelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        final ListView listViewChannels = (ListView)findViewById(R.id.listViewChannels);

        Button buttonAmis = (Button)findViewById(R.id.buttonAmis);
        buttonAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        String token = settings.getString("accesstoken", "");

        AsyncTaskClass async = new AsyncTaskClass();
        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                Gson gson = new Gson();
                channelList = gson.fromJson(response, ResponseChannelList.class);
                listViewChannels.setAdapter(new ChannelArrayAdapter(getApplicationContext(), channelList.getChannels()));
                listViewChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ResponseChannel channel = channelList.getChannels().get(position);
                        Intent myIntent2 = new Intent(getApplicationContext(), ChannelActivity.class);
                        myIntent2.putExtra("channelID", channel.getChannelID());
                        startActivity(myIntent2);
                    }
                });
            }
        });
        async.execute("http://www.raphaelbischof.fr/messaging/?function=getchannels", "accesstoken", token);
    }
}
