package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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


public class InfoActivity extends Activity {
    public static String author;
    ListView mBookList;
    TextView authorText;
    ArrayList<Book> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActionBar actionBar = super.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayShowHomeEnabled(true);
        ImageView iV = (ImageView) findViewById(R.id.imageView2);
        author = HomeActivity.getAuthor();
        mBookList = (ListView) findViewById(R.id.bookslistView);
        authorText = (TextView) findViewById(R.id.authorTextView);
        authorText.setTypeface(HomeActivity.getFont());
        authorText.setText(author);
        Log.d("***APPANAME:", "" + author);
        String data = "";
        try {
            data = new GetInfo().execute(author).get();
        }
        catch (Exception e){
        }
        bookSearch(data);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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

    public void bookSearch(String data) {
        al = new ArrayList<Book>();
        ArrayList tags = new ArrayList();
        String authorname = "",isbn = "",booktitle = "",year = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jb = jsonObject.getJSONArray("docs");
            for (int i = 0; i < jb.length(); i++){
                JSONObject jarr = jb.getJSONObject(i);
                try {
                    booktitle = jarr.getString("title");//Title
                    booktitle = booktitle.replaceAll("\",", "");
                    Log.d("***APPANAME:", "" + booktitle);
                }catch (Exception e){}

                try{
                isbn = jarr.getJSONArray("isbn").getString(0); //ISBN
                isbn = isbn.replaceAll("\",", "");
                Log.d("***APPANAME:", "" + isbn);
                }catch (Exception e){}

                try{
                authorname = jarr.getJSONArray("author_name").getString(0);//Author
                authorname = authorname.replaceAll("\",", "");
                Log.d("***APPANAME:", "" + authorname);
                }catch (Exception e){}
                try{
                JSONArray tagsarray = jarr.getJSONArray("subject"); //tags
                for (int y = 0; y<tagsarray.length(); y++){
                    String tagname = tagsarray.getString(y);
                    tagname = tagname.replaceAll("\",", "");
                    Log.d("***APPANAME:", "" + tagname);
                    tags.add(tagname);
                }
                }catch (Exception e){}
                try{
                year = jarr.getString("first_publish_year");
                year = year.replaceAll("\",", "");
                Log.d("***APPANAME:", "" + year);
                }catch (Exception e){}
                Book newbook = new Book(booktitle,authorname,isbn,year,tags);
                if(!al.contains(newbook)) {
                    al.add(newbook);
                }
            }
            //updatelist(al);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class GetInfo extends AsyncTask<String, Void, String> {
        private Exception e;

        protected String doInBackground(String... query) {
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

        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
}