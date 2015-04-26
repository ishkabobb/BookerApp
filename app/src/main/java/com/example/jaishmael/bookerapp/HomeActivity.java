package com.example.jaishmael.bookerapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    myDBHandler mDBHandler;
    private static SearchView mSearch;
    ListView lv;
    private static String author;
    public static Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDBHandler = new myDBHandler(this,null,null,1);
        mSearch = (SearchView)findViewById(R.id.searchView);
        lv = (ListView) findViewById(R.id.listView);
        ActionBar actionBar = super.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        font = Typeface.createFromAsset(getAssets(), "fonts/Tangerine_Bold.ttf");
        initList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) lv.getItemAtPosition(position);
                author = temp;
                String a = "'";
                if (author.contains(a)){
                    author.replaceAll(a,"%20");
                }
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                intent.putExtra("query", author);
                HomeActivity.this.startActivity(intent);

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) lv.getItemAtPosition(position);
                author = temp;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                builder1.setMessage("Remove "+temp+" from Tracker?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDBHandler.deleteAuthor(author);
                                initList();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
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
        yourAdapter<String> mAdapter = new yourAdapter(this, al);
        lv.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onResume(){
        super.onResume();
        initList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public static String getAuthor(){
        return author;
    }
    public static Typeface getFont(){
        return font;
    }
}

class yourAdapter<String> extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
       return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView text = (TextView) vi.findViewById(R.id.authorName);
        text.setTypeface(HomeActivity.getFont());
        text.setText(data.get(position).toString());
        return vi;
    }
}
