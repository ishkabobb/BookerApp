package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HomeActivity extends Activity {
    //Final Git Test

    myDBHandler mDBHandler;
    private static SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSearch = (SearchView)findViewById(R.id.searchView);
        Log.d("app:", "*******Home Activity Started");
        ActionBar actionBar = super.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowHomeEnabled(true);
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


        initList();

        // We get the ListView component from the layout

        ListView lv = (ListView) findViewById(R.id.listView);

        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        SimpleAdapter simpleAdpt = new SimpleAdapter(this, bookList, android.R.layout.simple_list_item_1, new String[] {"key"}, new int[] {android.R.id.text1});

        lv.setAdapter(simpleAdpt);
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {

                // We know the View is a TextView so we can cast it
                TextView clickedView = (TextView) view;

                Toast.makeText(HomeActivity.this, "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();

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

    // The data to show
    List<Map<String, String>> bookList = new ArrayList<Map<String,String>>();

    private void initList() {
        // We populate the books

        bookList.add(createBook("key", "Book 1"));
        bookList.add(createBook("key", "Book 2"));
        bookList.add(createBook("key", "Book 3"));
        bookList.add(createBook("key", "Book 4"));
        bookList.add(createBook("key", "Book 5"));
        bookList.add(createBook("key", "Book 6"));
        bookList.add(createBook("key", "Book 7"));

    }

    private HashMap<String, String> createBook (String key, String name) {
        HashMap<String, String> planet = new HashMap<String, String>();
        planet.put(key, name);

        return planet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
}
