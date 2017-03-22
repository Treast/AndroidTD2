package riva.vincent.channelmessaging.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import riva.vincent.channelmessaging.R;

public class FriendsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        userDataSource.open();
        final List<Friend> friends = userDataSource.getAllFriends();
        userDataSource.close();
        ListView listViewFriends = (ListView)findViewById(R.id.listViewAmis);
        listViewFriends.setAdapter(new FriendArrayAdapter(getApplicationContext(), friends));

        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = friends.get(position);

                Intent myIntent = new Intent(getApplicationContext(), FriendMessageActivity.class);
                myIntent.putExtra("userid", friend.getUid());
                startActivity(myIntent);
            }
        });
    }

}
