package riva.vincent.channelmessaging;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import riva.vincent.channelmessaging.channel.MessageArrayAdapter;
import riva.vincent.channelmessaging.fragments.SoundRecordDialog;
import riva.vincent.channelmessaging.listeners.OnCompleteRequestListener;
import riva.vincent.channelmessaging.response.ResponseMessage;
import riva.vincent.channelmessaging.response.ResponseMessageList;
import riva.vincent.channelmessaging.async.UploadFileToServer;
import riva.vincent.channelmessaging.friends.UserDataSource;
import riva.vincent.channelmessaging.async.AsyncTaskClass;

public class ChannelActivity extends GPSActivity {

    private String accesstoken;
    private int channelID;
    private Context context;
    private final int PICTURE_REQUEST_CODE = 1001;
    private ListView listViewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        this.context = this;
        this.channelID = getIntent().getIntExtra("channelID", 1);

        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
        this.accesstoken = settings.getString("accesstoken", "");

        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        Button buttonEnvoyer = (Button)findViewById(R.id.buttonEnvoyer);
        Button buttonPhoto = (Button)findViewById(R.id.buttonPhoto);
        Button buttonSon = (Button)findViewById(R.id.buttonSon);
        this.listViewMessages = (ListView)findViewById(R.id.listViewMessages);

        buttonSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundRecordDialog soundDialog = new SoundRecordDialog();
                soundDialog.show(getFragmentManager(), "Tag pour le son");
            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cache.bmp");
                Uri u = Uri.fromFile(f);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, PICTURE_REQUEST_CODE);
            }
        });

        buttonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async2 = new AsyncTaskClass();
                String message = editTextMessage.getText().toString();
                editTextMessage.setText("");

                if(mCurrentLocation != null)
                    async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", accesstoken, "channelid", channelID+"", "message", message, "latitude", mCurrentLocation.getLatitude()+"", "longitude", mCurrentLocation.getLongitude()+"");
                else
                    async2.execute("http://www.raphaelbischof.fr/messaging/?function=sendmessage", "accesstoken", accesstoken, "channelid", channelID+"", "message", message);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICTURE_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "cache.bmp");

               try {
                   resizeFile(file, getApplicationContext());
                } catch(Exception e)
                {
                    Log.d("Error", "ERRROOOOOOOOOOOOR");
                }
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.d("Test", "Test");
                this.channelID = getIntent().getIntExtra("channelID", 1);

                HashMap<String, String> hash = new HashMap<>();
                hash.put("accesstoken", this.accesstoken);
                hash.put("channelid", this.channelID+"");
                UploadFileToServer upload = new UploadFileToServer(this, file.getAbsolutePath(), hash, new UploadFileToServer.OnUploadFileListener() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("Result", result);
                    }

                    @Override
                    public void onFailed(IOException error) {
                            Log.d("Error", error.getMessage());
                    }
                });
                upload.execute();
            }
        }
    }

    private void resizeFile(File f,Context context) throws IOException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
        //The new size we want to scale to
        final int REQUIRED_SIZE=400;
        //Find the correct scale value. It should be the power of 2.
        int scale=1;
        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
            scale*=2;
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        int i = getCameraPhotoOrientation(context, Uri.fromFile(f),f.getAbsolutePath());
        if (o.outWidth>o.outHeight)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(i); // anti-clockwise by 90 degrees
            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
        }
        try {
            f.delete();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) throws IOException {
        int rotate = 0;
        context.getContentResolver().notifyChange(imageUri, null);
        File imageFile = new File(imagePath);
        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        return rotate;
    }

    private void refreshMessages() {
        final AsyncTaskClass async = new AsyncTaskClass();

        async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
            @Override
            public void onCompleteRequest(final String response) {
                if (response != "") {
                    Gson gson = new Gson();
                    ResponseMessageList messageList = gson.fromJson(response, ResponseMessageList.class);
                    listViewMessages.setAdapter(new MessageArrayAdapter(getApplicationContext(), messageList.getMessages()));
                    listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(response != "") {
                                final ResponseMessage message = (ResponseMessage) listViewMessages.getItemAtPosition(position);
                                AlertDialog dialog = new AlertDialog.Builder(ChannelActivity.this)
                                        .setIcon(android.R.drawable.ic_dialog_alert)//drawable de l'icone à gauche du titre
                                        .setTitle("Que voulez-vous faire .")//Titre de l'alert dialog
                                        .setItems(new String[]{"Ajouter en ami", "Voir sur la carte"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {
                                                    UserDataSource userDataSource = new UserDataSource(getApplicationContext());
                                                    userDataSource.open();
                                                    userDataSource.createFriend(message.getUsername(), message.getImageUrl(), message.getUserID());
                                                    userDataSource.close();
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                                                    intent.putExtra("lat", message.getLatitude());
                                                    intent.putExtra("lon", message.getLongitude());
                                                    intent.putExtra("user", message.getUsername());
                                                    startActivity(intent);
                                                }
                                            }
                                        }).create();
                                dialog.show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
            async.execute("http://www.raphaelbischof.fr/messaging/?function=getmessages", "accesstoken", this.accesstoken, "channelid", channelID + "");
    }

}
