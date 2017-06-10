package com.example.dawid.snake;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.dawid.snake.database.*;
import com.example.dawid.snake.R;

public class HighScoreActivity extends AppCompatActivity {

    private DatabaseHelper mDbHelper = new DatabaseHelper(this);
    private CustomAdapter customAdapter;
    private SQLiteDatabase db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_high_score);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        customAdapter = new CustomAdapter(this, getCursorForCustomAdapter());
        listView.setAdapter(customAdapter);
        registerForContextMenu(listView);
    }

    private Cursor getCursorForCustomAdapter()
    {
        db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.DB._ID,
                DatabaseContract.DB.COLUMN_NICK,
                DatabaseContract.DB.COLUMN_POINTS,
                DatabaseContract.DB.COLUMN_LEVEL,
        };
        String orderBy = DatabaseContract.DB.COLUMN_POINTS;
        Cursor cursor = db.query(
                DatabaseContract.DB.TABLE_PERSONS,
                projection,
                null,
                null,
                null,
                null,
                DatabaseContract.DB.COLUMN_LEVEL +" DESC, " + DatabaseContract.DB.COLUMN_POINTS +" DESC"
        );
        return cursor;
    }


    private class CustomAdapter extends CursorAdapter // potrzebne do poprawnego wyświetlania się listView
    {
        private LayoutInflater mLayoutInflater; // dynamicznie ładowany layout z xmla za pomocą layoutInflater na systemie android

        public CustomAdapter(Context context, Cursor cursor)
        {
            super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            View view = mLayoutInflater.inflate(R.layout.activity_list_of_person, parent, false);
            return view;
        }

        @Override
        public void bindView(View v, Context context, Cursor cursor)
        {
            String nick = cursor.getString(cursor.getColumnIndex(DatabaseContract.DB.COLUMN_NICK));
            int points = cursor.getInt(cursor.getColumnIndex(DatabaseContract.DB.COLUMN_POINTS));
            TextView textViewNick= (TextView) v.findViewById(R.id.textViewNick);
            TextView textViewPoints = (TextView) v.findViewById(R.id.textViewPoints);
            textViewNick.setText(nick);
            if(cursor.getInt(cursor.getColumnIndex(DatabaseContract.DB.COLUMN_LEVEL)) == 1)
            {
                textViewPoints.setText("Poziom trudności: łatwy, Liczba punktów: " + points);
            }
            else if(cursor.getInt(cursor.getColumnIndex(DatabaseContract.DB.COLUMN_LEVEL)) == 2)
            {
                textViewPoints.setText("Poziom trudności: średni, Liczba punktów: " + points);
            }
            else if(cursor.getInt(cursor.getColumnIndex(DatabaseContract.DB.COLUMN_LEVEL)) == 3)
            {
                textViewPoints.setText("Poziom trudności: trudny, Liczba punktów: " + points);
            }
        }
    }
}
