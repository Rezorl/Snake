package com.example.dawid.snake;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawid.snake.database.DatabaseContract;
import com.example.dawid.snake.database.DatabaseHelper;

public class SumaryActivity extends AppCompatActivity {
    public final static String DELAY = "com.example.dawid.snake.delay";
    private DatabaseHelper mDbHelper = new DatabaseHelper(this);
    private EditText editTextNick;
    private TextView textViewSum;
    private int points;
    private int delay;
    private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sumary);

        textViewSum = (TextView)findViewById(R.id.textViewSum);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        points = intent.getIntExtra(GameActivity.POINTS,0);
        level = intent.getIntExtra(GameActivity.LEVEL,level);
        delay = intent.getIntExtra(GameActivity.DELAY,0);
        textViewSum.setText("Koniec gry... \n Suma zdobytych punktów: \n" + points);
        editTextNick = (EditText)findViewById(R.id.editTextNick);
    }

    public void buttonAddScoreClick(View view)
    {
        if(editTextNick.getText().length() ==0)
        {
            Toast.makeText(this,"Nick nie może być pusty.",Toast.LENGTH_SHORT).show();
        }
        else
            {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.DB.COLUMN_NICK, editTextNick.getText().toString());
            values.put(DatabaseContract.DB.COLUMN_POINTS, points);
            values.put(DatabaseContract.DB.COLUMN_LEVEL, level);
            if (db.insert(DatabaseContract.DB.TABLE_PERSONS,null,values) == -1)
            {
                Toast.makeText(this,"Błąd podczas dodawania danych do bazy.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Dodano wynik do bazy danych.",Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(DELAY,delay);
            startActivity(intent);
        }
    }

    public void buttonExitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DELAY,delay);
        startActivity(intent);
    }
}
