package com.example.tarun.filmbuff;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView list;
    Button next,previous;
    TextView pageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageStatus = findViewById(R.id.pageStatus);

        next = (Button)findViewById(R.id.firstPage);
        previous = findViewById(R.id.previous);


        list = (ListView)findViewById(R.id.list);
        list.setOnItemClickListener(this);

        final int[] page = {1};

        new checkConnectionStatus().execute("https://api.themoviedb.org/3/movie/popular?api_key=d745c3b81e0fdc9589414b237ae6a0ad&language=en-US&page="+String.valueOf(page[0]));
        pageStatus.setText("Showing Page : 1 of 992");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page[0] = page[0] + 1;
                pageStatus.setText("Showing Page :"+page[0]+ " of 992");
                new checkConnectionStatus().execute("https://api.themoviedb.org/3/movie/popular?api_key=d745c3b81e0fdc9589414b237ae6a0ad&language=en-US&page="+String.valueOf(page[0]));




            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page[0]>1) {
                    page[0] = page[0] - 1;
                    pageStatus.setText("Showing Page :" + page[0] + " of 992");
                    new checkConnectionStatus().execute("https://api.themoviedb.org/3/movie/popular?api_key=d745c3b81e0fdc9589414b237ae6a0ad&language=en-US&page=" + String.valueOf(page[0]));


                }else{
                    Toast.makeText(MainActivity.this, "Page 0 not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this,MovieInformation.class);
        intent.putExtra("MOVIE_DETAILS",(MovieDetails)parent.getItemAtPosition(position));
        startActivity(intent);

    }

    class checkConnectionStatus extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                 url = new URL(strings[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try{
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(s);

                ArrayList<MovieDetails> movieList = new ArrayList<>();

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails();

                    movieDetails.setOriginal_title(object.getString("original_title"));
                    movieDetails.setVote_average(object.getDouble("vote_average"));
                    movieDetails.setOverview(object.getString("overview"));
                    movieDetails.setRelease_date(object.getString("release_date"));
                    movieDetails.setPoster_path(object.getString("poster_path"));
                    movieList.add(movieDetails);
                }
                MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(MainActivity.this,R.layout.movie_list,movieList);
                list.setAdapter(movieArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
