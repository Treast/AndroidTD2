package riva.vincent.channelmessaging.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;

import riva.vincent.channelmessaging.R;

/**
 * Created by rivav on 22/03/2017.
 */
public class SoundRecordDialog extends DialogFragment {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    public Dialog dialog = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Son")
                .setMessage("Voulez-vous enregistrer un son ?")
                .setPositiveButton(R.string.enregistrer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFileName = getActivity().getExternalCacheDir().getAbsolutePath();
                        mFileName += "/audiorecordtest.3gp";

                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setOutputFile(mFileName);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        try {
                            mRecorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mRecorder.start();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecorder.stop();
                                mRecorder.reset();
                                mRecorder.release();

                                try {
                                    mPlayer.setDataSource(mFileName);
                                    mPlayer.prepare();
                                    mPlayer.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        this.dialog = builder.create();
        return this.dialog;
    }
}
