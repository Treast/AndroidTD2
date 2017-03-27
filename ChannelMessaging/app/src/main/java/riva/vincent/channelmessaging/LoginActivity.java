package riva.vincent.channelmessaging;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import java.util.Random;

import riva.vincent.channelmessaging.async.AsyncTaskClass;
import riva.vincent.channelmessaging.channel.ChannelListActivity;
import riva.vincent.channelmessaging.listeners.OnCompleteRequestListener;
import riva.vincent.channelmessaging.response.ResponseAccessToken;

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


                YoYo.with(Techniques.SlideOutRight).duration(750).withListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        textViewWinter.setText(explainStringArray[new Random().nextInt(explainStringArray.length)]);
                        YoYo.with(Techniques.SlideInLeft).duration(750).playOn(textViewWinter);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).playOn(textViewWinter);
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
                        if(response != "") {
                            Gson gson = new Gson();
                            textViewWinter.clearAnimation();
                            ResponseAccessToken token = gson.fromJson(response, ResponseAccessToken.class);

                            loader.setVisibility(View.INVISIBLE);
                            buttonValider.setVisibility(View.VISIBLE);
                            if (token.getCode() == 200) {
                                SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("accesstoken", token.getAccesstoken());
                                editor.commit();
                                startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, imageViewLogo, "logo").toBundle());
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Connexion échouée", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Réessayer", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editTextUsername.setText("");
                                        editTextPassword.setText("");
                                    }
                                });
                                snackbar.show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.INVISIBLE);
                            buttonValider.setVisibility(View.VISIBLE);
                        }
                    }
                });
                    SharedPreferences settings = getSharedPreferences("MyPrefs", 0);

                    String firebaseToken = settings.getString("firebaseToken", "dd");
                    Log.d("Firebase Token", firebaseToken);
                    if(firebaseToken != "")
                        async.execute("http://www.raphaelbischof.fr/messaging/?function=connect", "username", editTextUsername.getText().toString(), "password", editTextPassword.getText().toString(), "registrationid", firebaseToken);
                    else
                        async.execute("http://www.raphaelbischof.fr/messaging/?function=connect", "username", editTextUsername.getText().toString(), "password", editTextPassword.getText().toString());
            }
        });
    }
}
