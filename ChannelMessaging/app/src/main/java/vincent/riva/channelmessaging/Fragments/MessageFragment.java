package vincent.riva.channelmessaging.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import vincent.riva.channelmessaging.AsyncTaskClass;
import vincent.riva.channelmessaging.ChannelActivity;
import vincent.riva.channelmessaging.ChannelListActivity;
import vincent.riva.channelmessaging.MessageArrayAdapter;
import vincent.riva.channelmessaging.OnCompleteRequestListener;
import vincent.riva.channelmessaging.R;
import vincent.riva.channelmessaging.ResponseMessage;
import vincent.riva.channelmessaging.ResponseMessageList;
import vincent.riva.channelmessaging.UserDataSource;

public class MessageFragment extends Fragment {

    private Button buttonEnvoyer;
    private Button buttonPhoto;
    private EditText editTextMessage;
    private String token;
    private int channelID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        this.editTextMessage = (EditText)v.findViewById(R.id.editTextMessage);
        this.buttonEnvoyer = (Button)v.findViewById(R.id.buttonEnvoyer);
        this.buttonPhoto = (Button)v.findViewById(R.id.buttonPhoto);

        this.buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async2 = new AsyncTaskClass();
                String message = editTextMessage.getText().toString();
                editTextMessage.setText("");
                async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", token, "channelid", channelID+"", "message", message);
            }
        });
  /*      final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                (ChannelActivity)getActivity().refreshMessages();
                handler.postDelayed(this, 5000);
            }
        };
        r.run();*/
        return v;
    }

    public void setUp(String token, int channelID)
    {
        this.token = token;
        this.channelID = channelID;
    }
}
