package vincent.riva.channelmessaging.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import vincent.riva.channelmessaging.MapActivity;
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
    private ListView listViewMessages;
    private String token;
    private int channelID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        listViewMessages = (ListView) v.findViewById(R.id.listViewMessages);
        this.editTextMessage = (EditText) v.findViewById(R.id.editTextMessage);
        this.buttonEnvoyer = (Button) v.findViewById(R.id.buttonEnvoyer);
        this.buttonPhoto = (Button) v.findViewById(R.id.buttonPhoto);
        this.channelID = getActivity().getIntent().getIntExtra("channelID", 1);

        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
        this.token = settings.getString("accesstoken", "");

        this.buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async2 = new AsyncTaskClass();
                String message = editTextMessage.getText().toString();
                editTextMessage.setText("");
                async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", token, "channelid", channelID + "", "message", message);
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
        return v;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    private void refreshMessages() {
        final AsyncTaskClass async = new AsyncTaskClass();

        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(String response) {
                if (getActivity() != null) {
                    Gson gson = new Gson();
                    ResponseMessageList messageList = gson.fromJson(response, ResponseMessageList.class);
                    listViewMessages.setAdapter(new MessageArrayAdapter(getActivity().getApplicationContext(), messageList.getMessages()));
                    listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final ResponseMessage message = (ResponseMessage) listViewMessages.getItemAtPosition(position);
                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(android.R.drawable.ic_dialog_alert)//drawable de l'icone à gauche du titre
                                    .setTitle("Que voulez-vous faire .")//Titre de l'alert dialog
                                    .setItems(new String[]{"Ajouter en ami", "Voir sur la carte"}, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                UserDataSource userDataSource = new UserDataSource(getActivity().getApplicationContext());
                                                userDataSource.open();
                                                userDataSource.createFriend(message.getUsername(), message.getImageUrl());
                                                userDataSource.close();
                                            } else {
                                                Intent intent = new Intent(getActivity().getApplicationContext(), MapActivity.class);
                                                intent.putExtra("lat", message.getLatitude());
                                                intent.putExtra("lon", message.getLongitude());
                                                intent.putExtra("user", message.getUsername());
                                                startActivity(intent);
                                            }
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });
                }
            }
        });

        async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", token, "channelid", channelID + "");
    }
}
