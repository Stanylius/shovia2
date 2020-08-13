package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentSection extends AppCompatActivity {
    FirebaseAuth fAuth;

    private RNotifViewModel rnotifVM;

    ProgressBar progressComment;
    ProgressDialog progressDialog;

    List<Review> reviewList;

    String userName,myReview;
    Float myRating;
    RelativeLayout commentLayout;
    EditText commentBox;
    RatingBar ratingUser;
    Button submitButton;
    TextView backButton;

    static String movieId,movieTitle;



    ReviewAdapter adapter;
    RecyclerView recyclerView;
    static String URL_REVIEW = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getreview/";
    static String URL_INPUT_REVIEW = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/review";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);
        fAuth = FirebaseAuth.getInstance();
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.Back);
        commentBox = findViewById(R.id.commentBox);
        commentLayout = findViewById(R.id.commentLayout);
        ratingUser = findViewById(R.id.ratingUser);
        submitButton = findViewById(R.id.submitButton);
        progressComment = findViewById(R.id.progressComment);
        recyclerView = findViewById(R.id.recylerComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(savedInstanceState == null){
            commentLayout.setVisibility(View.INVISIBLE);
            progressComment.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Movie movie = bundle.getParcelable("Movie");
        userName = bundle.getString("userName");
        movieId = movie.getId();
        movieTitle = movie.getTitle();

        reviewList = new ArrayList<Review>();

        LoadReview();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputComment(fAuth.getCurrentUser().getUid());
                progressDialog = new ProgressDialog(CommentSection.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rnotifVM = ViewModelProviders.of(this).get(RNotifViewModel.class);



    }
    private void LoadReview(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_REVIEW+movieId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for(int i = 0; i<products.length();i++){
                                JSONObject productObj = products.getJSONObject(i);
                                String rating = productObj.getString("rating");
                                String comment = productObj.getString("review");
                                JSONObject val = productObj.getJSONObject("data");
                                String name = val.getString("name");
                                Review review = new Review(name, comment, rating);
                                reviewList.add(review);
                            }
                            commentLayout.setVisibility(View.VISIBLE);
                            progressComment.setVisibility(View.INVISIBLE);
                            adapter = new ReviewAdapter(CommentSection.this, reviewList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentSection.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void inputComment(final String userID){
        myRating = ratingUser.getRating();
        myReview = commentBox.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INPUT_REVIEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Review review = new Review(userName, myReview, String.valueOf(myRating));
                        reviewList.add(0,review);
                        //Notif notif = new Notif();
                        //notif.inserting(userID,userName,"Anda telah Memberikan Review Pada Filem ini");
                        RNotif rNotif = new RNotif(userID,movieTitle,"Anda telah Memberikan Review Pada Filem ini");
                        rnotifVM.insert(rNotif);
                        adapter.notifyDataSetChanged();
                        commentBox.setText("");
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        commentBox.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(CommentSection.this, "Anda sudah pernah mereview filem ini", Toast.LENGTH_SHORT).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid",userID);
                params.put("movieid",movieId);
                params.put("rating", String.valueOf(myRating));
                params.put("review",myReview);

                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
