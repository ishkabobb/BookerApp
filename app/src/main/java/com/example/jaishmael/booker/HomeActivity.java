package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
        Log.d("app:", "*******Home Activity Started");
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
                //Toast.makeText(getApplicationContext(), "Selected " + temp, Toast.LENGTH_SHORT).show();     //Not Needed
                //initList();
                author = temp;
                String a = "'";
                if (author.contains(a)){
                    author.replaceAll(a,"%20");
                }
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);                   //Broken
                intent.putExtra("query", author);                                                    //Broken
                HomeActivity.this.startActivity(intent);                                             //Broken

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) lv.getItemAtPosition(position);
                author = temp;
                //Toast.makeText(getApplicationContext(), "Author " + temp +" has been Deleted.", Toast.LENGTH_SHORT).show();
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
                //initList();
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
    public static Typeface getFont(){
        return font;
    }
}
class yourAdapter<String> extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView text = (TextView) vi.findViewById(R.id.authorName);
        text.setTypeface(HomeActivity.getFont());
        text.setText(data.get(position).toString());

        return vi;
    }
}
