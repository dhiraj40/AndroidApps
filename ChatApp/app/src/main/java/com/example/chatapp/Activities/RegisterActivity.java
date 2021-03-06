package com.example.chatapp.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatapp.R;
import com.example.chatapp.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;
    Typeface MR, MRR;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        username.setTypeface(MRR);
        email.setTypeface(MRR);
        password.setTypeface(MRR);
        btn_register.setTypeface(MR);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(view -> {
            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();
            Utils.hideKeyboard(RegisterActivity.this);

            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6 ){
                Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                register(txt_username, txt_email, txt_password);
            }
        });
    }

    private void register(final String username, String email, String password){

        dialog = Utils.showLoader(RegisterActivity.this);

        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
        if (task.isSuccessful()){
            FirebaseUser firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            String userid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userid);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("username", username);
            hashMap.put("imageURL", "default");
            hashMap.put("status", "offline");
            hashMap.put("bio", "");
            hashMap.put("search", username.toLowerCase());
            if(dialog!=null){dialog.dismiss(); }
            reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()){
                    Intent intent = new Intent(RegisterActivity.this,
                            MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
                    } else {
                        Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });
    }
}
