package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    myDBHandler mDBHandler;
    private static SearchView mSearch;
    ListView lv;
    private static String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDBHandler = new myDBHandler(this,null,null,1);
        mSearch = (SearchView)findViewById(R.id.searchView);
        Log.d("app:", "*******Home Activity Started");
        lv = (ListView) findViewById(R.id.listView);
        ActionBar actionBar = super.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        initList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) lv.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "Selected " + temp, Toast.LENGTH_SHORT).show();     //Not Needed
                initList();
                author = temp;
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);                   //Broken
                intent.putExtra("query", author);                                                    //Broken
                HomeActivity.this.startActivity(intent);                                             //Broken

            }
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchableActivity.class);
                intent.putExtra("query", query);
                HomeActivity.this.startActivity(intent);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchableActivity.class);
                intent.putExtra("query", query);
                HomeActivity.this.startActivity(intent);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void initList() {
        ArrayList<String> al = mDBHandler.databaseToString();
        Log.d("***ALSIZE:", "" + al.size());
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onResume(){
        super.onResume();
        initList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static String getAuthor(){
        return author;
    }
}
