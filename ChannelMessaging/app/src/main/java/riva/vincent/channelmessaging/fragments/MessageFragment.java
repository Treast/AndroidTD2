package riva.vincent.channelmessaging.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import riva.vincent.channelmessaging.async.AsyncTaskClass;
import riva.vincent.channelmessaging.MapActivity;
import riva.vincent.channelmessaging.channel.MessageArrayAdapter;
import riva.vincent.channelmessaging.listeners.OnCompleteRequestListener;
import riva.vincent.channelmessaging.R;
import riva.vincent.channelmessaging.response.ResponseMessage;
import riva.vincent.channelmessaging.response.ResponseMessageList;
import riva.vincent.channelmessaging.friends.UserDataSource;

public class MessageFragment extends Fragment {

    private Button buttonEnvoyer;
    private Button buttonPhoto;
    private Button buttonSon;
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
        this.buttonSon = (Button) v.findViewById(R.id.buttonSon);
        this.channelID = getActivity().getIntent().getIntExtra("channelID", 1);

        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", 0);
        this.token = settings.getString("accesstoken", "");

        this.buttonSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundRecordDialog soundDialog = new SoundRecordDialog();
                soundDialog.show(getActivity().getFragmentManager(), getTag());
            }
        });

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
                if (getActivity() != null && isOnline()) {
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
                                                userDataSource.createFriend(message.getUsername(), message.getImageUrl(), message.getUserID());
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
        if(isOnline())
            async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", token, "channelid", channelID + "");
        else
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
