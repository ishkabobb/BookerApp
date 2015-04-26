package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchableActivity extends Activity {
    public static String returnedresult;
    ListView mListView;
    ArrayList<String> al;
    myDBHandler mDBHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        ActionBar actionBar = super.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        Log.d("app:", "Started");
        mListView = (ListView)findViewById(R.id.searchlistView);
        mDBHandler = new myDBHandler(this, null, null, 1);
        returnedresult = "";
        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchManager.QUERY);
            //GoodReads API Key: kO8qbnq9i1TznNmTy7kSRA
            //GoodReads Secret API Key: BKnbOex3bXD1WcVT25PeCJvErjSkrP3YWA9AAsnVbnI
            //must display goodreads logo
        getRequest(query);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) mListView.getItemAtPosition(position);
                Author a = new Author(temp);
                if(!mDBHandler.addAuthor(a)){
                    Toast.makeText(getApplicationContext(), "Item already exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Added Author: " + temp, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchableActivity.this, HomeActivity.class);
                    SearchableActivity.this.startActivity(intent);
                };
            }
        });
    }

    public void updatelist(ArrayList al){

        searchAdapter<String> mAdapter = new searchAdapter<String>(this, al);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_searchable, menu);
        return true;
    }

    public void getRequest(String query){ //place holder
        String data = "no";
        try {
            data = new RunSearch().execute(query).get();
        }
        catch (Exception e){
        }
        myAuthorsSearch(data);
    }

    public void myAuthorsSearch(String data) {
        al = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jb = jsonObject.getJSONArray("docs");
            for (int i = 0; i < jb.length(); i++){
                JSONObject jarr = jb.getJSONObject(i);
                String author = jarr.getString("author_name");
                author = author.substring(2,author.length()-2);
                if(!al.contains(author)) {
                    al.add(author);
                    Log.d("***APPANAME:", "" + author);
                }
            }
            updatelist(al);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    //private class
    private class RunSearch extends AsyncTask<String, Void, String> {
        private Exception e;

        protected String doInBackground(String ...query){
              StringBuilder builder = new StringBuilder();
                HttpClient client = new DefaultHttpClient();
                String search = query[0].replaceAll("\\s+", "%20");
                String request = "http://openlibrary.org/search.json?author=" + search;
                request = request + "&jscmd=data&format=json";

                HttpGet httpGet = new HttpGet(request);
                try {
                    HttpResponse response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } else {
                        Log.e("APP", "Failed to download file");
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return builder.toString();
            }
        protected void onPostExecute(String result){
        }
        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }

}
class searchAdapter<String> extends BaseAdapter {

    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public searchAdapter(Context context, ArrayList<String> data) {
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
            vi = inflater.inflate(R.layout.row2, null);
        TextView text = (TextView) vi.findViewById(R.id.authorNameSearch);
        text.setTypeface(HomeActivity.getFont());
        text.setText(data.get(position).toString());

        return vi;
    }
}
