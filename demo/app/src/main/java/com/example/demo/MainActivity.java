package com.example.demo;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Locale;
import android.os.Handler;
import java.util.TimerTask;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {


    private TextToSpeech mTTS;
    private TextView maxSpeedTextView;
    private TextView currentSpeedTextView;
    private Switch vibrationSwitch;
    private Switch audioSwitch;
    private Handler handler;
    private Runnable runnable;


    //private int delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("notifications error", "Language not supported");
                    } else {
                        // Timer calls function every 5 second.
                        timer(5000);
                    }
                } else {
                    Log.e("notifications error", "Initialization failed");
                }
            }
        });
    }

    // Calls a function ms times. ms = millisecond , 1000 millisecond = 1 second
    protected void timer(int ms) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textToSpeech("Slow Down");
                        vibrate();
                    }
                });
            }
        }, 0, ms);
    }

    // Checks to see if the vibration switch is on. If it is on it vibrates.
    protected void vibrate() {
        vibrationSwitch = findViewById(R.id.vibrationSwitch);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrationSwitch.isChecked()) {
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createOneShot(750, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    // Checks to see if the audio switch is on. If it is on it talks.
    protected void textToSpeech(String textWarning) {
        audioSwitch = getAudioSwitch();
        if (audioSwitch.isChecked()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTTS.speak(textWarning, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }


    protected Switch getAudioSwitch()
    {
        return findViewById(R.id.audioSwitch);
    }

    protected Switch getVibrationSwitch()
    {
        return findViewById(R.id.vibrationSwitch);
    }


}
