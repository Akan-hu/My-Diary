package android.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText forgotEmail;
    Button recoverBtn;
    Button backBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotEmail = findViewById(R.id.recover_email);
        recoverBtn = findViewById(R.id.btn_recover);
        backBtn = findViewById(R.id.btn_back_login);

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = forgotEmail.getText().toString().trim();
                if(mail.isEmpty()){
                    forgotEmail.setError("Please enter Email");
                }
                else{
                     firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {

                             if(task.isSuccessful()){
                                 Toast.makeText(getApplicationContext(), "Please check your email to recover your password", Toast.LENGTH_SHORT).show();
                                 finish();
                             }
                             else{
                                 Toast.makeText(getApplicationContext(), "This email is not registered", Toast.LENGTH_SHORT).show();

                             }

                         }
                     });

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}