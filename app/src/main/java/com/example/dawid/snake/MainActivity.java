package com.example.dawid.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private int delay;
    private boolean isChanged;
    public static final String DELAY="com.example.dawid.snake.delay";
    public static final String IS_CHANGE="com.example.dawid.snake.isChange";
    public int saveDelay = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        isChanged = intent.getBooleanExtra(LevelActivity.IS_CHANGE,false);
        if(isChanged)
        {
            delay = intent.getIntExtra(LevelActivity.DELAY,0);
        }
        if(delay ==0)
        {
            intent = getIntent();
            delay = intent.getIntExtra(SumaryActivity.DELAY,0);
        }
        }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        isChanged = intent.getBooleanExtra(LevelActivity.IS_CHANGE,false);
        if(isChanged)
        {
            delay = intent.getIntExtra(LevelActivity.DELAY,0);
        }
    }

    /*@Override
    public void onStop() {
        super.onStop();
        saveDelay = delay;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDelay = delay;
    }*/

    public void buttonStartNewGameClick(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        if(isChanged)
        {
            intent.putExtra(DELAY,delay);
            intent.putExtra(IS_CHANGE,isChanged);
        }
        startActivity(intent);
    }

    public void buttonOpenHighScore(View view)
    {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }

    public void buttonSetLevel(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(IS_CHANGE,false);
        intent.putExtra(DELAY,delay);
        startActivity(intent);
    }


}
