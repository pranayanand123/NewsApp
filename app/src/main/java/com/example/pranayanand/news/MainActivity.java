package com.example.pranayanand.news;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.R.attr.x;

public class MainActivity extends AppCompatActivity {
    int i=0;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> titles;
    NewsAdapter adapter;
    ArrayList<String> urls;
    ArrayList<String> urlsToImage;
    ArrayList<String> descriptions;
    CardView cardView;
    ArrayList<String> articleContents;

    public MainActivity() throws ExecutionException, InterruptedException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);


        titles = new ArrayList<String>();
        urls = new ArrayList<String>();
        urlsToImage = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        cardView = (CardView) findViewById(R.id.cardView);
        articleContents = new ArrayList<String>();
        adapter = new NewsAdapter(androidColors, titles, descriptions, urls,urlsToImage, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        sqLiteDatabase = this.openOrCreateDatabase("NEWS", MODE_PRIVATE, null);
        String cmd = "CREATE TABLE IF NOT EXISTS NEWS ( id INTEGER PRIMARY KEY, title VARCHAR, description VARCHAR, url VARCHAR, urlToImage VARCHAR, articleContent VARCHAR)";
        sqLiteDatabase.execSQL(cmd);






        DownloadTask task = new DownloadTask();

        task.execute("https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=latest&apiKey=API_KEY");



    }





public void updateListView(){
    Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM NEWS", null);
    int idIndex = c.getColumnIndex("id");
    int titleIndex = c.getColumnIndex("title");
    int descriptionIndex = c.getColumnIndex("description");
    int urlIndex = c.getColumnIndex("url");
    int urlToImageIndex = c.getColumnIndex("urlToImage");

    titles.clear();
    c.moveToFirst();
    do {
        titles.add(c.getString(titleIndex));
        urls.add(c.getString(urlIndex));
        urlsToImage.add(c.getString(urlToImageIndex));
        descriptions.add(c.getString(descriptionIndex));

    } while (c.moveToNext());
    adapter.notifyDataSetChanged();
}

public class DownloadTask extends AsyncTask<String,Void, String>{
    String result = "";

        @Override
        protected String doInBackground(String... URLS) {

                URL jsonurl;
                HttpURLConnection connection;

                try {

                    jsonurl = new URL(URLS[0]);
                    connection = (HttpURLConnection) jsonurl.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    int data = inputStreamReader.read();
                    while (data != -1) {
                        char current = (char) data;
                        result += current;
                        data = inputStreamReader.read();
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }




                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("articles"));
                    sqLiteDatabase.execSQL("DELETE FROM NEWS");


                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = new JSONObject(jsonArray.getString(i));
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String url = jsonObject1.getString("url");
                        String urlToImage = jsonObject1.getString("urlToImage");




                        Log.i("Title", title);
                        Log.i("description", description);
                        Log.i("url", url);
                        Log.i("urlToImage", urlToImage);

                        String sql = "INSERT INTO NEWS (id, title, description, url, urlToimage) VALUES (?,?,?,?,?)";
                        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
                        statement.bindString(1, String.valueOf(i));
                        statement.bindString(2, title);
                        statement.bindString(3, description);
                        statement.bindString(4, url);
                        statement.bindString(5, urlToImage);

                        statement.execute();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }







            return null;
        }

    @Override
    protected void onPostExecute(String s) {
        updateListView();

    }
}


}
