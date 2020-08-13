package umn.ac.shovia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class addSQLite extends AppCompatActivity {
    private RNotifViewModel rnotifVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rnotifVM = ViewModelProviders.of(this).get(RNotifViewModel.class);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("uid");
        String title = bundle.getString("name");
        String text = bundle.getString("text");
        RNotif rNotif = new RNotif(id,title,text);
        rnotifVM.insert(rNotif);
        setResult(RESULT_OK);
        finish();

    }

}
