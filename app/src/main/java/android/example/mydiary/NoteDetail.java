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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NoteDetail extends AppCompatActivity {

    TextView noteTitle, noteContent;
    FloatingActionButton noteEdit;
    Toolbar detailToolbar;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        noteTitle = findViewById(R.id.detail_title);
        noteContent = findViewById(R.id.detail_note);
        noteEdit = findViewById(R.id.edit_note);
        detailToolbar = findViewById(R.id.detail_toolbar);

        setSupportActionBar(detailToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

         data = getIntent();

        noteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditNote.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteid",data.getStringExtra("noteid"));
                v.getContext().startActivity(intent);
                finish();
            }

        });
        noteTitle.setText(data.getStringExtra("title"));
        noteContent.setText(data.getStringExtra("content"));

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}