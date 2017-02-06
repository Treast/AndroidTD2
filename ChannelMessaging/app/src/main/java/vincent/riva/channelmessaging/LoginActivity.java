package vincent.riva.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonValider = (Button)findViewById(R.id.buttonValider);
        final EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        final Intent myIntent = new Intent(this, ChannelListActivity.class);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass async = new AsyncTaskClass();
                async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
                    @Override
                    public void onCompleteRequest(String response) {
                        Gson gson = new Gson();
                        ResponseAccessToken token = gson.fromJson(response, ResponseAccessToken.class);
                        SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("accesstoken", token.getAccesstoken());
                        editor.commit();
                        startActivity(myIntent);
                    }
                });
                async.execute("http://www.raphaelbischof.fr/messaging/?function=connect", "username", editTextUsername.getText().toString(), "password", editTextPassword.getText().toString());
            }
        });
    }
}
