package vincent.riva.channelmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class FriendsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        userDataSource.open();
        List<Friend> friends = userDataSource.getAllFriends();
        userDataSource.close();
        ListView listViewFriends = (ListView)findViewById(R.id.listViewAmis);
        listViewFriends.setAdapter(new FriendArrayAdapter(getApplicationContext(), friends));
    }

}
