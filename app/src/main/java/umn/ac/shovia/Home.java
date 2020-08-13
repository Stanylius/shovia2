package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements TextWatcher{
    private static String URL_GETALL = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getmovies";
    private static String URL_GETUSER = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/user/";
    String userId,userName,Phone;
    RecyclerView recyclerView;
    ProgressBar homeProgress;
    EditText search;
    LinearLayout ethomeparent;
    FirebaseAuth fAuth;

    JsonArrayRequest request;
    private RequestQueue requestQueue ;
    List<Movie> MovieList;
    MovieAdapter adapter;
    ImageView notip,nearby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /*Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        MovieList = (ArrayList<Movie>) args.getSerializable("ARRAYLIST");*/
        fAuth = FirebaseAuth.getInstance();
        notip = findViewById(R.id.notifBtn);
        homeProgress = findViewById(R.id.progressHome);
        ethomeparent = findViewById(R.id.Header);

        search = (EditText) findViewById(R.id.searchingMovie);
        nearby = findViewById(R.id.nearby);

        userId = fAuth.getCurrentUser().getUid();
        search.addTextChangedListener(this);
        MovieList = new ArrayList<Movie>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(savedInstanceState == null){
            ethomeparent.setVisibility(View.INVISIBLE);
            homeProgress.setVisibility(View.VISIBLE);
        }
        LoadUser();
        LoadData();
        notip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notifIntent = new Intent(Home.this,Notif.class);
                startActivity(notifIntent);
            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notifIntent = new Intent(Home.this,MapsActivity.class);
                startActivity(notifIntent);
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                return true;
            case R.id.Profile:
                Intent profileIntent = new Intent(Home.this,ProfileActivity.class);
                profileIntent.putExtra("name",userName).putExtra("phone",Phone);
                startActivity(profileIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LoadUser(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GETUSER+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject products = new JSONObject(response);
                            JSONObject val = products.getJSONObject("data");
                            userName = val.getString("name");
                            Phone = val.getString("phone");
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void LoadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GETALL,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray products = new JSONArray(response);

                    for(int i = 0; i<products.length();i++){
                        List<String> trailer = new ArrayList<String>();
                        JSONObject productObj = products.getJSONObject(i);
                        String id = productObj.getString("id");
                        JSONObject val = productObj.getJSONObject("data");
                        String poster = val.getString("poster");
                        String date = val.getString("release_date");
                        String title = val.getString("title");
                        String desc = val.getString("description");
                        String country = val.getString("release_country");
                        String duration = val.getString("duration");
                        String background = val.getString("background");
                        JSONArray traileryt = val.getJSONArray("youtube_link");
                        for(int j = 0; j < traileryt.length();j++ ){
                            trailer.add(traileryt.getString(j));

                        }
                        Movie movie = new Movie(id, poster, date, title,desc,country,duration,background,trailer);
                        MovieList.add(movie);
                    }
                    adapter = new MovieAdapter(Home.this, MovieList, userName);
                    recyclerView.setAdapter(adapter);
                    ethomeparent.setVisibility(View.VISIBLE);
                    homeProgress.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        this.adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
