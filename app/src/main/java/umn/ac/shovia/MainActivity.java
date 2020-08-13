package umn.ac.shovia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button linkregister, loginButton;
    FirebaseAuth fAuth;
    ProgressBar pgbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        fAuth = FirebaseAuth.getInstance();
        linkregister = (Button) findViewById(R.id.linkregister);
        pgbar = (ProgressBar) findViewById(R.id.pg_login);
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
        linkregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);

                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errCtr = 0;
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    errCtr = 1;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    errCtr = 2;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 characters");
                    errCtr = 3;
                }
                if(errCtr == 0){
                    pgbar.setVisibility(View.VISIBLE);
                    linkregister.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                pgbar.setVisibility(View.INVISIBLE);
                                linkregister.setVisibility(View.VISIBLE);
                                loginButton.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                                pgbar.setVisibility(View.INVISIBLE);
                                linkregister.setVisibility(View.VISIBLE);
                                loginButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

            }
        });


    }

}
