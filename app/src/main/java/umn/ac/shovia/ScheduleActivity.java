package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private Spinner spinner1;
    private Button btnSubmit;
    List<Schedule> ScheduleList;
    ScheduleAdapter adapter;
    RecyclerView rv;
    ProgressBar pgSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ScheduleList = new ArrayList<Schedule>();
        rv = findViewById(R.id.recylerSchedule);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        pgSchedule = findViewById(R.id.pgSchedule);

        addListenerOnButton();
    }
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ScheduleList.clear();
                Toast.makeText(ScheduleActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
                pgSchedule.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                jsonParse(String.valueOf(spinner1.getSelectedItem()));
            }

        });
    }

    private void jsonParse(String location) {
        String url = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/getschedule?movie=4GtGix5rdNjEa2ajwofL&location="+location;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject datas = jsonArray.getJSONObject(i);
                                List<String> waktu = new ArrayList();
                                JSONArray schedule = datas.getJSONArray("schedule");
                                for (int j = 0; j <schedule.length(); j++){
                                    waktu.add(schedule.getString(j));
                                }
                                String name = datas.getString("name");
                                Schedule objSchedule = new Schedule(waktu, name);
                                ScheduleList.add(objSchedule);
                            }

                            ScheduleList.size();
                            adapter = new ScheduleAdapter(ScheduleActivity.this, ScheduleList);
                            rv.setAdapter(adapter);
                            pgSchedule.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}
