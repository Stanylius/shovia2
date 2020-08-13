package umn.ac.shovia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Notif extends AppCompatActivity {
    RecyclerView recyclerView;
    private RNotifViewModel rnotifVM;
    private static final int REQUEST_TAMBAH = 1;
    TextView back;
    FirebaseAuth fAuth;
    ImageView delAll;

    private static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        back = findViewById(R.id.titleNotif);
        fAuth = FirebaseAuth.getInstance();
        delAll = findViewById(R.id.delAll);
        uid = fAuth.getCurrentUser().getUid();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        final NotifListAdapter adapter = new NotifListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rnotifVM = ViewModelProviders.of(this).get(RNotifViewModel.class);
        rnotifVM.getDaftarRNotif().observe(this,
                new Observer<List<RNotif>>() {
                    @Override
                    public void onChanged(List<RNotif> rnotifs) {
                        adapter.setRNotif(rnotifs);
                    }
                });

        delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rnotifVM.deleteAll();
            }
        });
    }


}
