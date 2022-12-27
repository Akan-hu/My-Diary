package android.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    Button button1;
    Button button2;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText1 = findViewById(R.id.reg_name);
        editText2 = findViewById(R.id.reg_mail);
        editText3 = findViewById(R.id.reg_pass);
        button1 = findViewById(R.id.btn_reg);
        button2 = findViewById(R.id.btn_login);

        firebaseAuth = FirebaseAuth.getInstance();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String regName = editText1.getText().toString().trim();
                String regEmail = editText2.getText().toString().trim();
                String regPass = editText3.getText().toString().trim();

                if(regName.isEmpty() || regEmail.isEmpty() || regPass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All fields are required to fill", Toast.LENGTH_SHORT).show();
                }
                else if(regPass.length()<7){
                    Toast.makeText(RegisterActivity.this, "Please enter minimum 7 digits", Toast.LENGTH_SHORT).show();
                }

                else{

                    firebaseAuth.createUserWithEmailAndPassword(regEmail,regPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                //Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                //to check that entered email is correct we send a verification code to entered email address
                                emailVerification();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }

    //sending email verification
    private void emailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(RegisterActivity.this, "Verification link is sent please verify and login with us", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            });

        }
        else{

            Toast.makeText(RegisterActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();

        }
    }
}