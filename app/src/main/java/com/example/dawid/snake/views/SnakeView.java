package com.example.dawid.snake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dawid.snake.enums.TileType;

/**
 * Created by Dawid on 15.05.2017.
 */

public class SnakeView extends View
{
    private Paint mPaint = new Paint();
    private TileType snakeViewMap[][];

    public SnakeView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnakeViewMap(TileType[][] map)
    {
        this.snakeViewMap = map;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(snakeViewMap != null)
        {
            float tileSizeX = canvas.getWidth() / snakeViewMap.length;
            float tileSizeY = canvas.getHeight() / snakeViewMap[0].length;
            float circleSize = Math.min(tileSizeX,tileSizeY) / 2;


            for(int i = 0; i < snakeViewMap.length; ++i)
            {
               for(int j=0; j < snakeViewMap[i].length; ++j)
               {
                   switch (snakeViewMap[i][j])
                   {
                       case Nothing:
                           mPaint.setColor(Color.parseColor("#8fc925"));
                           break;
                       case Wall:
                           mPaint.setColor(Color.parseColor("#006600"));
                           break;
                       case SnakeHead:
                           mPaint.setColor(Color.parseColor("#edeece"));
                           break;
                       case SnakeTail:
                           mPaint.setColor(Color.parseColor("#558201"));
                           break;
                       case Apple:
                           mPaint.setColor(Color.rgb(229,229,0));
                           break;
                       case Worms:
                           mPaint.setColor(Color.rgb(244,144,96));
                           break;
                   }
                   canvas.drawCircle(i * tileSizeX + tileSizeX /2f + circleSize / 2, j * tileSizeY + tileSizeY /2f + circleSize /2, circleSize,mPaint);
               }
            }
        }
    }
}