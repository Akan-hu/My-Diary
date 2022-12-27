package android.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPass;
    TextView forgotPass;
    Button login;
    Button register;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginEmail = findViewById(R.id.log_email);
        loginPass = findViewById(R.id.log_pass);
        forgotPass = findViewById(R.id.forgot_pass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.login_progress);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      /*  if(firebaseUser != null){
            finish();
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                           if(task.isSuccessful()){
                               checkEmailVerification();
                           }
                           else{
                               Toast.makeText(MainActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                               progressBar.setVisibility(View.INVISIBLE);

                           }

                        }
                    });

                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);

            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else{

            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Registered email is not verified", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }

    }
}