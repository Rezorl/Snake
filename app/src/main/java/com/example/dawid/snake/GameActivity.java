package com.example.dawid.snake;

import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.dawid.snake.engine.GameEngine;
import com.example.dawid.snake.enums.Direction;
import com.example.dawid.snake.enums.GameState;
import com.example.dawid.snake.views.SnakeView;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    public final static String POINTS = "com.example.dawid.snake.points";
    public final static String LEVEL = "com.example.dawid.snake.level";
    public final static String DELAY = "com.example.dawid.snake.delay";
    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private long updateDelay = 200;
    private float prewX, prewY;
    int clickCount = 0;
    long startTime;
    long duration;
    private boolean onPause = false;
    static final int MAX_DURATION = 450;
    private boolean isChanged;
    int steps =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameEngine = new GameEngine();
        gameEngine.initGame();
        snakeView = (SnakeView)findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);
        startUpdateHandler();
        Intent intent = getIntent();
        isChanged = intent.getBooleanExtra(MainActivity.IS_CHANGE,false);
        if(isChanged)
        {
            updateDelay = intent.getIntExtra(MainActivity.DELAY,0);
        }
        gameEngine.setSpeed(updateDelay);
    }

    @Override
    public void onPause() {
        super.onPause();
        gameEngine.setCurrentGameState(GameState.Pause);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameEngine.setCurrentGameState(GameState.Running);
    }

    private void startUpdateHandler()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();
                ++steps;
                if(steps == 10)
                {
                    steps=0;
                    gameEngine.setChange(true);
                }
                if(gameEngine.getCurrentGameState() == GameState.Running)
                {
                    handler.postDelayed(this, updateDelay);
                }
                if(gameEngine.getCurrentGameState() == GameState.Lost)
                {
                    onGameLost();
                }
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }
    private void onGameLost()
    {
        Toast.makeText(this,"Przegrałeś.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SumaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(POINTS,gameEngine.getNumberOfApplesCollected());
        intent.putExtra(DELAY,updateDelay);
        if(updateDelay == LevelActivity.LEVEL_EASY)
        {
            intent.putExtra(LEVEL,1);
        }
        else if(updateDelay == LevelActivity.LEVEL_MEDIUM)
        {
            intent.putExtra(LEVEL,2);
        }
        else if(updateDelay == LevelActivity.LEVEL_HARD)
        {
            intent.putExtra(LEVEL,3);
        }
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                prewX = motionEvent.getX();
                prewY = motionEvent.getY();
                if(clickCount==0)
                {
                    startTime = System.currentTimeMillis();
                }
                clickCount++;
                break;
            case MotionEvent.ACTION_UP:
                float newX = motionEvent.getX();
                float newY = motionEvent.getY();
                //double tap
                long time = System.currentTimeMillis() - startTime;
                duration=  duration + time;
                if(clickCount == 2)
                {
                    if(duration<= MAX_DURATION)
                    {
                        if(onPause)
                        {
                            gameEngine.setCurrentGameState(GameState.Running);
                            startUpdateHandler();
                            Toast.makeText(this,"Wznowiono grę.",Toast.LENGTH_SHORT).show();
                            onPause = false;
                        }
                        else
                        {
                            gameEngine.setCurrentGameState(GameState.Pause);
                            Toast.makeText(this,"Wstrzymano grę.",Toast.LENGTH_SHORT).show();
                            onPause = true;
                        }
                    }
                    clickCount = 0;
                    duration = 0;
                }

                //obliczanie gdzie jestesmy
                if(!onPause)
                {
                    if (Math.abs(newX - prewX) > Math.abs(newY - prewY))
                    {
                        //lewo - prawo
                        if (newX > prewX)
                        {
                            //prawo
                            gameEngine.updateDirection(Direction.EAST);
                        } else
                            {
                            //lewo
                            gameEngine.updateDirection(Direction.WEST);
                        }
                    } else
                        {
                        //gora - dol
                        if (newY < prewY)
                        {
                            //gora
                            gameEngine.updateDirection(Direction.NORTH);
                        } else
                            {
                            //dol
                            gameEngine.updateDirection(Direction.SOUTH);
                        }
                    }
                }
                break;
        }
        return true;
    }
}
