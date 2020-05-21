package com.example.favoritewebsites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> titles= new ArrayList<>();
    static ArrayList<String> urls= new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    EditText titleEditText;
    EditText urlEditText;
    Button addButton;
    SQLiteDatabase websitesDB;

    public void buttonClicked(View view){
        String strTitle=titleEditText.getText().toString();
        String strUrl=urlEditText.getText().toString();
        Log.i("STR",strTitle);
        Log.i("URL",strUrl);

        websitesDB.execSQL("INSERT INTO websites2 (title,url) VALUES ('"+strTitle+"','"+strUrl+"')");
        updateListView();

    }

    public void updateListView() {
        titles.clear();
        urls.clear();
        try {
            Cursor c = websitesDB.rawQuery("SELECT * FROM websites2", null);
            int urlIndex = c.getColumnIndex("url");
            int titleIndex = c.getColumnIndex("title");
            c.moveToFirst();
            while (!c.isAfterLast()) {
                titles.add(c.getString(titleIndex));
                urls.add(c.getString(urlIndex));
                c.moveToNext();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        websitesDB=this.openOrCreateDatabase("websites",MODE_PRIVATE,null);
        websitesDB.execSQL("CREATE TABLE IF NOT EXISTS websites2 (title VARCHAR, url VARCHAR)");


        titleEditText=findViewById(R.id.titleEditText);
        urlEditText=findViewById(R.id.urlEditText);
        addButton=findViewById(R.id.addButton);

        ListView listView= findViewById(R.id.listView);

        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1,titles);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WebsiteActivity.class);
                intent.putExtra("urlId",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToDelete= position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this website?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                websitesDB.execSQL("DELETE FROM websites2 WHERE title='"+titles.get(itemToDelete)+"'");
                               updateListView();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });

       updateListView();




    }
}
