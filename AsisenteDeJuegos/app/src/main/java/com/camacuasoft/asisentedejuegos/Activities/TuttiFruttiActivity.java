package com.camacuasoft.asisentedejuegos.Activities;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.camacuasoft.asisentedejuegos.Models.LetterList;
import com.camacuasoft.asisentedejuegos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TuttiFruttiActivity extends AppCompatActivity {

    private LetterList letterList;
    private TextView usedLetterText;
    private TextView currentLetterText;
    private TextView chronoText;
    private FrameLayout letterFrame;

    private boolean letterRuning = false;
    private boolean alarmFinish = true;
    private boolean chronoEnabled = false;
    private CountDownTimer letterTimer;
    private CountDownTimer chronoTimer;
    private int letterIndex;
    private List<String> usedLetterList;
    private long chronoInitialTime = 0;

    boolean isGreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutti_frutti);

        //Menu
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Bind UI
        usedLetterText = (TextView) findViewById(R.id.tutifruti_used_letters);
        currentLetterText = (TextView) findViewById(R.id.tutifruti_current_letter);
        chronoText = (TextView) findViewById(R.id.tutifruti_chrono);


        letterList = new LetterList();

        usedLetterList = new ArrayList<>();

        //Initialize UI
        usedLetterText.setText("");
        currentLetterText.setText(letterList.getLetter(0));
        chronoText.setText("00:00");

        letterTimer = new CountDownTimer(120000, 120) {
            @Override
            public void onTick(long millisUntilFinished) {
                letterIndex = new Random().nextInt(letterList.getSize());
                currentLetterText.setText(letterList.getLetter(letterIndex));
            }

            @Override
            public void onFinish() {
                letterRuning = false;
                alarmFinish = true;
            }
        };

        setChronoTimer(chronoInitialTime);

        currentLetterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letterRun();
            }
        });

        chronoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronoTimerSelection();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_new_game:
                letterTimer.cancel();
                letterList = new LetterList();
                usedLetterList = new ArrayList<String>();
                usedLetterText.setText("");
                currentLetterText.setText(letterList.getLetter(0));
                letterRuning = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setChronoTimer(long millis)
    {
        chronoTimer = new CountDownTimer(millis, 120) {
            @Override
            public void onTick(long millisUntilFinished) {
                setChronoText(millisUntilFinished);

                if(millisUntilFinished < 10000)
                {
                    if(isGreen)
                    {
                        chronoText.setBackgroundResource(R.color.colorRedBackround);
                        isGreen = false;
                    } else {
                        chronoText.setBackgroundResource(R.color.colorPrimaryLight);
                        isGreen = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                try {
                    Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vuvuzelatrumpet); //RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentLetterText.setBackgroundResource(R.color.colorRedBackround);
                alarmFinish = true;
            }
        };
    }

    private void letterRun()
    {
        if(letterRuning)
        {
            String auxStr = "";
            letterRuning = false;
            letterTimer.cancel();
            usedLetterList.add(letterList.getLetter(letterIndex));
            currentLetterText.setText(letterList.getAndRemove(letterIndex));
            for (int i = 0; i < usedLetterList.size(); i++) {
                auxStr += (usedLetterList.get(i) + " ");
            }
            usedLetterText.setText(auxStr);
            if(chronoEnabled)
                chronoTimer.start();
        } else {
            if(letterList.getSize() > 0) {
                if(alarmFinish) {
                    currentLetterText.setBackgroundResource(R.color.colorGreenBackground);
                    letterRuning = true;
                    letterTimer.start();
                    if(chronoEnabled)
                        alarmFinish = false;
                } else {
                    currentLetterText.setBackgroundResource(R.color.colorGreenBackground);
                    alarmFinish = true;
                    chronoTimer.cancel();
                }
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setTitle("Has utilizado todas las letras");
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        letterList = new LetterList();
                        usedLetterList = new ArrayList<String>();
                        usedLetterText.setText("");
                    }
                });
                dialogBuilder.show();
            }
        }
    }

    private void chronoTimerSelection(){
        AlertDialog.Builder chronoDialog = new AlertDialog.Builder(this);
        chronoDialog.setTitle("Elija un tiempo de juego")
                .setItems(R.array.chrono_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                chronoInitialTime = 0;
                                chronoEnabled = false;
                                break;
                            case 1:
                                chronoInitialTime = 60000;
                                chronoTimer.cancel();
                                setChronoText(chronoInitialTime);
                                setChronoTimer(chronoInitialTime);
                                chronoEnabled = true;
                                break;
                            case 2:
                                chronoTimer.cancel();
                                chronoInitialTime = 120000;
                                setChronoText(chronoInitialTime);
                                setChronoTimer(chronoInitialTime);
                                chronoEnabled = true;
                                break;
                            case 3:
                                chronoTimer.cancel();
                                chronoInitialTime = 180000;
                                setChronoText(chronoInitialTime);
                                setChronoTimer(chronoInitialTime);
                                chronoEnabled = true;
                                break;
                            case 4:
                                chronoTimer.cancel();
                                chronoInitialTime = 240000;
                                setChronoText(chronoInitialTime);
                                setChronoTimer(chronoInitialTime);
                                chronoEnabled = true;
                                break;
                            case 5:
                                chronoTimer.cancel();
                                chronoInitialTime = 300000;
                                setChronoText(chronoInitialTime);
                                setChronoTimer(chronoInitialTime);
                                chronoEnabled = true;
                                break;

                        }
                    }
                });
        chronoDialog.show();
    }

    private void setChronoText(long millis)
    {
        String chronoAux = "";
        int minutes = (int) millis / 60000;
        int seconds = (int) (millis % 60000) / 1000;
        chronoAux += (minutes < 10) ? "0" : "";
        chronoAux += (minutes + ":");
        chronoAux += (seconds < 10) ? "0" : "";
        chronoAux += seconds;
        chronoText.setText(chronoAux);
    }

}
