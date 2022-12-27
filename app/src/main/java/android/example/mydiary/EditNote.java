package android.example.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditNote extends AppCompatActivity {

    Toolbar editNoteToolbar;
    Intent data;
    EditText noteTitle,noteDescription;
    FloatingActionButton btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        noteTitle = findViewById(R.id.edit_title);
        noteDescription = findViewById(R.id.edit_note);
        btn = findViewById(R.id.edit_save_note);
        editNoteToolbar = findViewById(R.id.edit_toolbar);

        setSupportActionBar(editNoteToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        data = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = noteTitle.getText().toString();
                String newContent = noteDescription.getText().toString();

                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                           .collection("myNotes").document(data.getStringExtra("noteid"));

                Map<String,Object> editedNote = new HashMap<>();
                editedNote.put("Title",newTitle);
                editedNote.put("Content",newContent);
                documentReference.set(editedNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Your note is updated", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not update note", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        String myTitle = data.getStringExtra("title");
        String myContent = data.getStringExtra("content");
        noteTitle.setText(myTitle);
        noteDescription.setText(myContent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onBackPressed(){
        finish();
       super.onBackPressed();
    }
}