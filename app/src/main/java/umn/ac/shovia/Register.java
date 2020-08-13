package umn.ac.shovia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mEmail, mPassword, mFName, mPhone;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    Button registerButton;
    ProgressBar pgbar;
    private static String URL_REGIST = "https://us-central1-shovia-85fd5.cloudfunctions.net/main/api/v1/register";

    String userID, email, password, fullName, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pgbar = (ProgressBar) findViewById(R.id.pg_Regis);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mFName = (EditText) findViewById(R.id.fname);
        mPhone = (EditText) findViewById(R.id.phone);
        registerButton = (Button) findViewById(R.id.registerButton);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int errCtr = 0;
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                fullName = mFName.getText().toString().trim();
                phone = mPhone.getText().toString().trim();

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
                    registerButton.setVisibility(View.INVISIBLE);
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                Regist(userID);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                pgbar.setVisibility(View.INVISIBLE);
                                registerButton.setVisibility(View.VISIBLE);

                            }else{
                                Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                                pgbar.setVisibility(View.INVISIBLE);
                                registerButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

            }
        });

    }

    private void Regist(final String userID){
        email = mEmail.getText().toString().trim();
        fullName = mFName.getText().toString().trim();
        phone = mPhone.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Register.this, "Berhasil2", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Registerasi Failed!" +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid",userID);
                params.put("Email",email);
                params.put("Phone",phone);
                params.put("FullName",fullName);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
