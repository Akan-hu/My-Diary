package android.example.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                check();
                finish();

            }
        },3000);

    }

    private void check() {

        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){

           Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);

        }

        else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


    }
}