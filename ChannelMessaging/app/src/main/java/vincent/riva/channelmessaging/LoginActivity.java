package vincent.riva.channelmessaging;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.wang.avi.Indicator;

import org.w3c.dom.Text;

public class LoginActivity extends Activity {

    private static final String[] explainStringArray = {
            "Connecte toi pour chatter avec tes amis",
            "Winter is coming",
            "The cake is a lie",
            "42"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button buttonValider = (Button)findViewById(R.id.buttonValider);
        final EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        final ImageView imageViewLogo = (ImageView)findViewById(R.id.imageView);
        final Intent myIntent = new Intent(this, ChannelListActivity.class);
        final TextView textViewWinter = (TextView)findViewById(R.id.textViewWinter);

        final View loader = (View)findViewById(R.id.avi);

        final Handler mHandlerTada = new Handler();
        final int mShortDelay = 4000; //milliseconds

        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
                YoYo.with(Techniques.Tada).delay(500).playOn(imageViewLogo);
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                textViewWinter.startAnimation(animSlideLeft);

                buttonValider.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);

                AsyncTaskClass async = new AsyncTaskClass();
                async.setOnCompleteRequestListener(new OnCompleteRequestListener() {
                    @Override
                    public void onCompleteRequest(String response) {
                        Gson gson = new Gson();
                        textViewWinter.clearAnimation();
                        ResponseAccessToken token = gson.fromJson(response, ResponseAccessToken.class);

                        loader.setVisibility(View.INVISIBLE);
                        buttonValider.setVisibility(View.VISIBLE);
                        if(token.getCode() != 200+"")
                        {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Connexion échouée", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Réessayer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    editTextUsername.setText("");
                                    editTextPassword.setText("");
                                }
                            });
                            snackbar.show();
                        } else {
                            SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("accesstoken", token.getAccesstoken());
                            editor.commit();
                            startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, imageViewLogo, "logo").toBundle());
                        }

                    }
                });
                async.execute("http://www.raphaelbischof.fr/messaging/?function=connect", "username", editTextUsername.getText().toString(), "password", editTextPassword.getText().toString());
            }
        });
    }
}
