package com.example.tarun.filmbuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieInformation extends AppCompatActivity {

    ImageView poster;
    TextView date,overview,rating,title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);


        poster = (ImageView)findViewById(R.id.poster);
        date = (TextView)findViewById(R.id.date);
        overview = (TextView)findViewById(R.id.overview);
        rating = (TextView)findViewById(R.id.rating);
        title = (TextView)findViewById(R.id.title);

       Bundle extras = getIntent().getExtras();
       MovieDetails details = (MovieDetails) extras.getSerializable("MOVIE_DETAILS");

        if(details != null){
            Picasso.get().load("https://image.tmdb.org/t/p/w500/"+details.getPoster_path()).into(poster);
            title.setText(details.getOriginal_title());
            rating.setText(Double.toString (details.getVote_average()));
            overview.setText(details.getOverview());
            date.setText(details.getRelease_date());
        }
    }
}
