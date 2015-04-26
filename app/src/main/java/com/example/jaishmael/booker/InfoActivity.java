package com.example.jaishmael.booker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class InfoActivity extends Activity {
    public static String author;
    public static Book book;
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
        ArrayList<Book> books = bookSearch(data);
        infoAdapter mInAdapter = new infoAdapter(this, books);
        mBookList.setAdapter(mInAdapter);

        mBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book temp = (Book) mBookList.getItemAtPosition(position);
                setBook(temp);
                Intent intent = new Intent(InfoActivity.this, BookActivity.class);
                InfoActivity.this.startActivity(intent);
            }
        });


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

    public ArrayList<Book> bookSearch(String data) {
        al = new ArrayList<Book>();
        ArrayList tags = new ArrayList();
        String authorname = "",isbn = "",booktitle = "",year = "", cover = "", authorpic = "";
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
                    cover = jarr.getJSONArray("edition_key").getString(0);
                    cover = cover.replaceAll("\",", "");
                }catch (Exception e){ Log.d("***APPANAME:", "Failed to get Cover for " + booktitle);}

                try{
                    authorpic = jarr.getJSONArray("author_key").getString(0);
                    authorpic = authorpic.replaceAll("\",", "");
                }catch (Exception e){ Log.d("***APPANAME:", "Failed to get authorpic for " + booktitle);}

                try{
                    isbn = jarr.getJSONArray("isbn").getString(0); //ISBN
                    isbn = isbn.replaceAll("\",", "");
                }catch (Exception e){ Log.d("***APPANAME:", "Failed to get ISBN for " + booktitle);}

                try{
                    authorname = jarr.getJSONArray("author_name").getString(0);//Author
                    authorname = authorname.replaceAll("\",", "");
                }catch (Exception e){Log.d("***APPANAME:", "Failed to get Authorname for " + booktitle);}

                try{
                    JSONArray tagsarray = jarr.getJSONArray("subject"); //tags
                    for (int y = 0; y<tagsarray.length(); y++){
                        String tagname = tagsarray.getString(y);
                        tagname = tagname.replaceAll("\",", "");
                        tags.add(tagname);
                    }
                }catch (Exception e){Log.d("***APPANAME:", "Failed to get tags for " + booktitle);}

                try{
                    year = jarr.getString("first_publish_year");
                    year = year.replaceAll("\",", "");
                }catch (Exception e){Log.d("***APPANAME:", "Failed to get year for " + booktitle);}

                Book newbook = new Book(booktitle,authorname,isbn,year, cover,tags);
                if(!al.contains(newbook)) {
                    al.add(newbook);
                }
            }
            new GetAuthorPic().execute(authorpic);
            new GetAuthorInfo().execute(authorpic);

            //updatelist(al);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return al;

    }

    public static void setBook(Book b){
        book = b;
    }
    public static Book getBook(){
        return book;
    }

    private class GetAuthorInfo extends AsyncTask<String, Void, String> {
        private Exception e;
        TextView tv = (TextView)findViewById(R.id.textViewDetails);

        protected String doInBackground(String... query) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String search = query[0];
            String request = "https://openlibrary.org/authors/" + search + ".json";


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

        protected void onPostExecute(String info) {
            String authorinfo ="", bio = "No Bio available.", death = "", birth = "";

            try {
                JSONObject jo = new JSONObject(info);

                   try {
                        bio = jo.getString("bio");//bio
                        bio = bio.replaceAll("\",", "");
                        } catch (Exception e) {
                    }

                    try {
                        death = jo.getString("death_date");
                        death = death.replaceAll("\",", "");
                    } catch (Exception e) {
                        Log.d("***APPANAME:", "Failed to get death");
                    }

                    try {
                        birth = jo.getString("birth_date");
                        birth = birth.replaceAll("\",", "");
                    } catch (Exception e) {
                        Log.d("***APPANAME:", "Failed to get birthday");
                    }
                }catch (Exception e){}

                authorinfo = bio;

                if (!birth.equals("")){
                    authorinfo = authorinfo + " Born: " + birth;
                }

                if (!death.equals("")){
                    authorinfo = authorinfo + " Died: " + death;
                }
               tv.setText(authorinfo);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    private class GetAuthorPic extends AsyncTask<String, Void, Bitmap> {
        private Exception e;
        ImageView img = (ImageView)findViewById(R.id.imageView2);

        protected Bitmap doInBackground(String... query) {
            Bitmap bitmap = null;
            try {
                String id = query[0];
                Log.d("***APPANAME:", "Authorpic id:  " + id);
                URL url = new URL ("http://covers.openlibrary.org/a/olid/" + id + "-M.jpg");
                Log.d("***APPANAME:", "Authorpic url:  " + url);
                HttpGet httpRequest = null;
                httpRequest = new HttpGet((url.toURI()));
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                bitmap = BitmapFactory.decodeStream(input);



            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap bm) {
            img.setImageBitmap(bm);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
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
class infoAdapter extends ArrayAdapter<Book> {
    View vi;
    Context context;
    ArrayList<Book> data;
    private static LayoutInflater inflater = null;
    Book b;

    public infoAdapter(Context context, ArrayList<Book> data) {
        super(context,0,data);
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
    public Book getItem(int position) {
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
        b = getItem(position);
        InfoActivity.setBook(b);
        vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row3, null);
        TextView text = (TextView) vi.findViewById(R.id.bookName);
        text.setTypeface(HomeActivity.getFont());
        ImageView ivCover = (ImageView) vi.findViewById(R.id.imageViewCover);
        text.setText(b.getTitle());
        return vi;

    }


}
