package com.swqube.knowmalaria;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import database.Music;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnOverview, btnHow;
    ImageButton btnExit, btnShare;
    boolean btnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = findViewById(R.id.btnPlay);
        btnOverview = findViewById(R.id.btnOverview);
        btnHow = findViewById(R.id.btnHow);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGame();
            }
        });

        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClicked = true;
                setOverview();
               }
        });

        btnHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClicked = true;
                setHowToPlay();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClicked = true;
                startActivity(new Intent(getApplicationContext(), LevelsActivity.class));
            }
        });

        /*
        play background music after 2secs
         */
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Music.play(getApplicationContext(), R.raw.knowmal, true);
            }
        }.start();
    }

    public void setOverview(){
        //create congratulation dialog display
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_about, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnNext = alertDialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        TextView txtHeader = alertDialog.findViewById(R.id.txtHeader);
        TextView txtHint = alertDialog.findViewById(R.id.txtHint);
        txtHeader.setText("OVERVIEW");
        txtHint.setText("Malaria is a life threatening disease of the tropics and affects 2 billion people globally per year, even though it is preventable and curable.\n\n" +
                "The disease is transmitted through the bite of infected female anopheles mosquito and can result in mild to severe illness. Children under five years, pregnant women and travellers from none malaria areas are most at risk of severe illness and death. It negatively affects socioeconomic development as well.  Adequate knowledge about malaria   is essential for effective control and eventual elimination of the disease.\n\n" +
                "Playing and progressing through the KnowMalaria game will furnish you with the required knowledge to make good decisions and take appropriate action to prevent and control malaria.\n\n" +
                "KnowMalaria is developed by the INTEDeC Research Group of the Department of Public Health, Federal University of Technology Owerri , Nigeria.");

    }

    public void setHowToPlay(){
        //create congratulation dialog display
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_about, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnNext = alertDialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        TextView txtHeader = alertDialog.findViewById(R.id.txtHeader);
        TextView txtHint = alertDialog.findViewById(R.id.txtHint);
        txtHeader.setText("HOW TO PLAY");
        txtHint.setText(Html.fromHtml("<p style=\"text-align: justify;\"><span style=\"font-size: 12.0pt; line-height: 115%;\">How well do you KnowMalaria? <strong>KnowMalaria</strong> is a thrilling quiz game designed to educate you on malaria prevention and control while providing entertainment. The game is made up of three difficulty levels: Easy, Medium and Hard. The questions become more brain tasking as you progress through the levels.</span></p>\n" +
                "<p style=\"text-align: justify;\"><span style=\"font-size: 12.0pt; line-height: 115%;\">For each question, there are four options; one correct option and 3 incorrect options. You score 5 points and gain 2 coins when you select the correct option that provides the right answer to a question. </span></p>\n" +
                "<p style=\"text-align: justify;\"><span style=\"font-size: 12.0pt; line-height: 115%;\">When stuck, you can use the 50:50 help option which eliminates two incorrect options, leaving you with one correct and one incorrect only. 50:50 takes 10 coins. </span></p>\n" +
                "<p style=\"text-align: justify;\"><strong><span style=\"font-size: 12.0pt; line-height: 115%;\">KnowMalaria</span></strong><span style=\"font-size: 12.0pt; line-height: 115%;\"> today and we can help Eradicate the disease because Knowledge is Power to do great things.</span></p>"));

    }

    @Override
    public void onStop(){
        super.onStop();
        if(!btnClicked)
            Music.stop(getApplicationContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        btnClicked = false;
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Music.play(getApplicationContext(), R.raw.knowmal, true);
            }
        }.start();

    }

    @Override
    public void onBackPressed(){
        exitGame();
    }

    public void exitGame(){
        /*
        Display an alertDialog to prompt player to confirm before exiting game
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_pause, null);
        builder.setView(dialogView).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtHeader = alertDialog.findViewById(R.id.txtHeader);
        txtHeader.setText("EXIT GAME?");
        Button btnCancel = alertDialog.findViewById(R.id.btnMenu);
        btnCancel.setText("CANCEL");
        Button btnExit = alertDialog.findViewById(R.id.btnResume);
        btnExit.setText("YES");
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

    }

    public void shareApp(){
        /*
        Display an alertDialog to prompt player to confirm before sharing app link
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_pause, null);
        builder.setView(dialogView).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtHeader = alertDialog.findViewById(R.id.txtHeader);
        txtHeader.setText("SHARE GAME?");
        Button btnCancel = alertDialog.findViewById(R.id.btnMenu);
        btnCancel.setText("CANCEL");
        Button btnExit = alertDialog.findViewById(R.id.btnResume);
        btnExit.setText("YES");
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                String url = "KnowMalaria is an interesting game about malaria and it control. You should try it\n\nhttp://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, url);
                share.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(Intent.createChooser(share, "Share via:"));
                alertDialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                return;
            }
        });
    }


}