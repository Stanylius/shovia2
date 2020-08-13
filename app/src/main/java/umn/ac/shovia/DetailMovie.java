package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailMovie extends YouTubeBaseActivity  {

    FirebaseAuth fAuth;
    String userName,myReview;
    Float myRating;
    RelativeLayout commentLayout;
    EditText commentBox;
    RatingBar ratingUser;
    Button  reviewButton;
    static String movieId,movieTitle;

    List<Detail> detailMovie;
    List<Review> reviewList;
    List<String> trailerList;

    ProgressDialog progressDialog;

    TextView backDetail,title,desc,country,dur;
    RecyclerView recyclerView, recyclerView2;
    ImageView bgImage,thumbnail, poster;
    CastAdapter adapter;
    TrailerAdapter adapter2;
    ReviewAdapter adapter3;

    Button scheduleBtn;

    static String URL_THUMBNAIL_H = "http://img.youtube.com/vi/";
    static String URL_THUMBNAIL_T = "/0.jpg";
    static String URL = "https://firebasestorage.googleapis.com/v0/b/shovia-85fd5.appspot.com/o";
    static String URL_CAST =  "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getcast/";
    static String URL_REVIEW = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getreview/";
    static String URL_INPUT_REVIEW = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/review";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        fAuth = FirebaseAuth.getInstance();
        scheduleBtn = findViewById(R.id.schedule);
        reviewButton = findViewById(R.id.ReviewButton);
        title = findViewById(R.id.title);
        country = findViewById(R.id.country);
        dur = findViewById(R.id.dur);
        poster = findViewById(R.id.posterDetail);
        bgImage = findViewById(R.id.detailBg);
        desc = findViewById(R.id.desc);
        backDetail = findViewById(R.id.DetailBack);
        recyclerView = findViewById(R.id.recylerCast);
        recyclerView2 = findViewById(R.id.recylerTrailer);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        reviewList = new ArrayList<Review>();
        detailMovie = new ArrayList<Detail>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Movie movie = bundle.getParcelable("Movie");
        userName = bundle.getString("userName");
        movieId = movie.getId();
        movieTitle = movie.getTitle();
        trailerList = new ArrayList<String>(movie.getTrailer());
        adapter2 = new TrailerAdapter(DetailMovie.this,trailerList);
        recyclerView2.setAdapter(adapter2);
        title.setText(movie.getTitle());
        country.setText(movie.getCountry());
        dur.setText(movie.getDuration());

        desc.setText(movie.getDesc());
        Picasso.get().load(URL+movie.getBackground()).fit().centerCrop().placeholder(R.drawable.noimage).into(bgImage);
        Picasso.get().load(URL+movie.getPoster()).fit().placeholder(R.drawable.noimage).into(poster);
        title.setText(movie.getTitle());
        backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LoadCast();
        //LoadReview();
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(DetailMovie.this, CommentSection.class);
                commentIntent.putExtra("Movie",movie).putExtra("userName",userName);
                startActivity(commentIntent);
            }
        });
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduleIntent = new Intent(DetailMovie.this, ScheduleActivity.class);
                scheduleIntent.putExtra("Movie",movie);
                startActivity(scheduleIntent);
            }
        });

    }
    private void LoadCast(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CAST+movieId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for(int i = 0; i<products.length();i++){
                                JSONObject productObj = products.getJSONObject(i);
                                String role = productObj.getString("role");
                                JSONObject val = productObj.getJSONObject("data");
                                String image = val.getString("Image");
                                String castName = val.getString("Name");
                                Detail detail = new Detail(castName,role,image);
                                detailMovie.add(detail);
                            }
                            adapter = new CastAdapter(DetailMovie.this, detailMovie);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailMovie.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
