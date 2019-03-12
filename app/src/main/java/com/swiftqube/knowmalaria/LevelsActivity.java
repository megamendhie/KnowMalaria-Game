package com.swiftqube.knowmalaria;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import database.Music;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnEasy, btnMedium, btnHard;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean easy, medium, hard;
    boolean btnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        btnEasy = findViewById(R.id.btnEasy); btnEasy.setOnClickListener(this);
        btnMedium = findViewById(R.id.btnMedium); btnMedium.setOnClickListener(this);
        btnHard = findViewById(R.id.btnHard); btnHard.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        btnClicked = false;
        Music.play(getApplicationContext(), R.raw.knowmal, true);
        easy = prefs.getBoolean("EASY", true);
        medium = prefs.getBoolean("MEDIUM", false);
        hard = prefs.getBoolean("HARD", false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEasy:
                startActivity(new Intent(this, LevelOneActivity.class));
                break;
            case R.id.btnMedium:
                if(!medium){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setMessage("Not in a hurry. You have to complete easy level first.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                else{
                    startActivity(new Intent(this, LevelTwoActivity.class));
                }
                break;
            case R.id.btnHard:
                if(!hard){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setMessage("Not in a hurry. You have to complete medium level first.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                else{
                    startActivity(new Intent(this, LevelThreeActivity.class));
                }
                break;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(!btnClicked)
            Music.stop(getApplicationContext());
    }

    @Override
    public void onBackPressed(){
        btnClicked = true;
        finish();
    }
}
