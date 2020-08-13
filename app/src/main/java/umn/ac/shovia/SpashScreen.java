package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpashScreen extends AppCompatActivity {
    private static String URL_GETALL = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getmovies";
    List<Movie> MovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        //MovieList = new ArrayList<>();

        //progressb.setMax(100);
        //startHeavyProcessing();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }


}
