package com.example.dawid.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LevelActivity extends AppCompatActivity {
    private RadioButton radioButtonLatwy;
    private RadioButton radioButtonSredni;
    private RadioButton radioButtonTrudny;
    private RadioGroup radioGroup;
    private int delay;
    public static final String DELAY="com.example.dawid.snake.delay";
    public static final String IS_CHANGE="com.example.dawid.snake.isChange";
    public static final int LEVEL_EASY = 200;
    public static final int LEVEL_MEDIUM = 100;
    public static final int LEVEL_HARD = 75;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level);
        radioButtonLatwy = (RadioButton)findViewById(R.id.radioButtonLatwy);
        radioButtonSredni = (RadioButton)findViewById(R.id.radioButtonSredni);
        radioButtonTrudny = (RadioButton)findViewById(R.id.radioButtonTrudny);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(MainActivity.IS_CHANGE,false))
        {
            if(intent.getIntExtra(MainActivity.DELAY,0) !=0)
            {
                if(intent.getIntExtra(MainActivity.DELAY,0) == LEVEL_HARD)
                {
                    radioButtonTrudny.setChecked(true);
                }
                else if(intent.getIntExtra(MainActivity.DELAY,0) == LEVEL_MEDIUM)
                {
                    radioButtonSredni.setChecked(true);
                }
                else if(intent.getIntExtra(MainActivity.DELAY,0) == LEVEL_EASY)
                {
                    radioButtonLatwy.setChecked(true);
                }
            }
        }
        else
        {
            radioButtonLatwy.setChecked(true);
        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(radioButtonLatwy.isChecked())
        {
            delay = LEVEL_EASY;
        }
        else if(radioButtonSredni.isChecked())
        {
            delay = LEVEL_MEDIUM;
        }
        else
        {
            delay = LEVEL_HARD;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DELAY,delay);
        intent.putExtra(IS_CHANGE,true);
        startActivity(intent);
    }
}
