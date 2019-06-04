package com.swqube.knowmalaria;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import database.Backend;
import database.DbProvider;
import database.Music;

public class LevelTwoActivity extends AppCompatActivity implements View.OnClickListener {
    ActionBar actionBar;
    TextView txtQ, txtScore, txtCoins;
    Button[] btnAnswer = new Button[5];
    ImageView[] imgLife = new ImageView[6];
    ImageView imgFifty, btnHome;
    ImageButton imgBg;
    int saved = 1, lenght = 0, num = 1, score = 80, coins, life=5;
    HashMap<String, Object> dataSet = new HashMap<>();
    String[] quest;
    String answer;
    DbProvider dbProvider;
    Random random = new Random();
    Backend backend;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);
        backend = new Backend(getApplicationContext());

        //create database provider
        dbProvider = new DbProvider(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        //initialize UI objects
        imgBg = findViewById(R.id.imgBg);
        txtQ = findViewById(R.id.txtQ);
        txtScore = findViewById(R.id.txtScore);
        txtCoins = findViewById(R.id.txtCoins);
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backend.setBackgroundImage(imgBg); //set image for activity header
        imgFifty = findViewById(R.id.imgFifty);imgFifty.setOnClickListener(this);
        btnAnswer[1] = findViewById(R.id.btnOne); btnAnswer[1].setOnClickListener(this);
        btnAnswer[2] = findViewById(R.id.btnTwo); btnAnswer[2].setOnClickListener(this);
        btnAnswer[3] = findViewById(R.id.btnThree); btnAnswer[3].setOnClickListener(this);
        btnAnswer[4] = findViewById(R.id.btnFour); btnAnswer[4].setOnClickListener(this);
        actionBar = getSupportActionBar();

        imgLife[1] = findViewById(R.id.imgLife1);
        imgLife[2] = findViewById(R.id.imgLife2);
        imgLife[3] = findViewById(R.id.imgLife3);
        imgLife[4] = findViewById(R.id.imgLife4);
        imgLife[5] = findViewById(R.id.imgLife5);

        lenght = dbProvider.getLength();
        coins = backend.getCoins();
        displayCoins(0);
    }

    @Override
    public void onResume(){
        super.onResume();
        dataSet = dbProvider.getLevelData(2);
        int savedGame = (int) dataSet.get("savedGame");
        if(savedGame==1){
            //checks if the level was saved, then gets saved data from database
            num = (int) dataSet.get("savedQuestion");
            score = (int) dataSet.get("savedScore");
            life = (int) dataSet.get("savedLife");

            //gets the saved question from database
            quest = dbProvider.getQuestions("Medium", num);
            txtScore.setText(score+"/180");
            setLife(life);
            display();
        }
        else{
            txtScore.setText("80/180");
            life=5;
            setLife(life);
            askQuesion();
        }
    }

    private void setLife(int life){
        if(life==0){
            for(int i= 1; i<=5; i++){
                imgLife[i].setImageResource(R.drawable.nolove);
            }
            return;
        }
        if(life==5){
            for(int i= 1; i<=5; i++){
                imgLife[i].setImageResource(R.drawable.love);
            }
            return;
        }
        for(int i= 1; i<=life; i++){
            imgLife[i].setImageResource(R.drawable.love);
        }
        for(int i =life+1; i<=5; i++){
            imgLife[i].setImageResource(R.drawable.nolove);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        backend.setCoins(coins);
        dbProvider.saveGame(2,1,saved,0,life,score,num);
    }

    public void askQuesion(){
        num = backend.Randomize(2);
        quest = dbProvider.getQuestions("Medium", num);
        display();
    }

    public void display(){
        txtQ.setText(quest[0]);
        ArrayList<Integer> options = new ArrayList<Integer>(4);
        for(int i=1; i<=4; i++){
            options.add(i);
        }
        for(int i=1; i<=4; i++){
            int n = random.nextInt(options.size());
            n = options.remove(n);
            btnAnswer[i].setText(quest[n]);
        }
        answer = quest[5];
    }

    public void displayCoins(int newCoins){
        coins = coins+newCoins;
        txtCoins.setText("$"+coins);
    }

    @Override
    public void onClick(View view) {
        Button button = null;
        switch (view.getId()){
            case R.id.btnOne:
                button = btnAnswer[1];
                break;
            case R.id.btnTwo:
                button = btnAnswer[2];
                break;
            case R.id.btnThree:
                button = btnAnswer[3];
                break;
            case R.id.btnFour:
                button = btnAnswer[4];
                break;
            case R.id.imgFifty:
                if(coins>=10){
                    displayCoins(-10);
                    imgFifty.setEnabled(false);
                    doFiftyFifty();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setMessage("Oops. \nYou don't have enough coins. You need at least 10 coins")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    enableButtons();
                                    setLife(life);
                                    //do nothing
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                return;
        }
        if(button.getText().toString().toLowerCase().equals(answer.toLowerCase())){
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            Music.answer(getApplicationContext(), R.raw.correct);
            enableButtons();
            addScore();
            displayCoins(2);
            if(score<180){
                askQuesion();
            }
            else {
                saved=0; score = 80; num = 0; life = 5;
                Congratulations();
            }
        }
        else {
            life-=1;
            life = Math.max(0,life);
            setLife(life);
            Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            Music.answer(getApplicationContext(), R.raw.wrong_sound);
            if(life<=0){
                saved=0; score = 80; num = 0; life = 5;
                endGame();
            }
        }
    }

    public void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage("Oops. \nYour life has finished. Try again?")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        askQuesion();
                        enableButtons();
                        txtScore.setText("80/180");
                        saved=1;
                        setLife(life);
                        //do nothing
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void Congratulations(){
        editor.putBoolean("HARD", true);
        editor.apply();

        //create congratulation dialog display
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.congratulations, null);
        builder.setView(dialogView).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnNext = alertDialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbProvider.saveGame(3,1,0,0,5,80,num);
                startActivity(new Intent(getApplicationContext(), LevelThreeActivity.class));
                finish();
            }
        });
        TextView txtHeader = alertDialog.findViewById(R.id.txtHeader);
        TextView txtHint = alertDialog.findViewById(R.id.txtHint);
        txtHeader.setText("You have completed this level.");
        txtHint.setText(Html.fromHtml("<p style=\"text-align: justify;\">How well do you know ITNs? Insecticide Treated Net (ITN) is a net (usually a bed net), that has been treated with safe and effective insecticides. " +
                "They are designed to prevent mosquito bites and are a form of personal protection against mosquitoes. " +
                "Efficient and effective use of ITN has been shown to reduce malaria occurrence, severe disease and death due to malaria especially in endemic regions.</p>"));
    }

    public void addScore(){
        score+=5;
        txtScore.setText(score+"/180");
    }

    //enable buttons disactivated during fifty_fifty
    private void enableButtons(){
        for(int i=1; i<=4; i++){
            btnAnswer[i].setEnabled(true);
        }
        imgFifty.setEnabled(true);
    }

    private void doFiftyFifty(){
        ArrayList<Integer> options = new ArrayList<Integer>(4);
        for(int i=1; i<=4; i++){
            options.add(i);
        }
        int k = 0;
        while (k<2){
            int n = random.nextInt(options.size());
            n = options.remove(n);
            Button button = btnAnswer[n];
            if(!button.getText().toString().toLowerCase().equals(answer.toLowerCase())){
                button.setText("");
                button.setEnabled(false);
                k++;
            }
        }
    }

    public void pausedMode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_pause, null);
        builder.setView(dialogView).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btnMenu = alertDialog.findViewById(R.id.btnMenu);
        Button btnResume = alertDialog.findViewById(R.id.btnResume);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                finish();
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onBackPressed(){
        pausedMode();
    }
}
