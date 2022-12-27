package android.example.mydiary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addNoteBtn;
    FirebaseAuth firebaseAuth;
    StaggeredGridLayoutManager gridLayout;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<NoteModel,NoteViewHolder> fireAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recycler);
        addNoteBtn = findViewById(R.id.floating_btn);

        Objects.requireNonNull(getSupportActionBar()).setTitle("All Notes");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseAuth = FirebaseAuth.getInstance();

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewNote.class));
            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                .collection("myNotes").orderBy("Title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<NoteModel> allUserNotes = new FirestoreRecyclerOptions.Builder<NoteModel>().setQuery(query, NoteModel.class).build();
        fireAdapter = new FirestoreRecyclerAdapter<NoteModel, NoteViewHolder>(allUserNotes) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull NoteModel noteModel) {

                int colCode = getRandomColour();
                noteViewHolder.noteLayout.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colCode,null));

                ImageView threeDot = noteViewHolder.itemView.findViewById(R.id.menu_pop_btn);

                noteViewHolder.noteTitle.setText(noteModel.getTitle());
                noteViewHolder.noteContent.setText(noteModel.getContent());

                //To fetch to id of note
                String noteId = fireAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.noteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(),NoteDetail.class);
                        intent.putExtra("title",noteModel.getTitle());
                        intent.putExtra("content",noteModel.getContent());
                        intent.putExtra("noteid",noteId);
                        v.getContext().startActivity(intent);

                    }
                });

                threeDot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //inbuilt function

                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(),EditNote.class);
                                intent.putExtra("title",noteModel.getTitle());
                                intent.putExtra("content",noteModel.getContent());
                                intent.putExtra("noteid",noteId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes").
                                        document(firebaseUser.getUid()).collection("myNotes").document(noteId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_single_row,parent,false);
                return new NoteViewHolder(view);
            }
        };

        recyclerView.setHasFixedSize(true);
        gridLayout = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setAdapter(fireAdapter);

    }

    private int getRandomColour() {

        List<Integer> noteColors = new ArrayList<>();
        noteColors.add(R.color.orange);
        noteColors.add(R.color.creme_color);
        noteColors.add(R.color.pink);
        noteColors.add(R.color.light_col_green);
        noteColors.add(R.color.yellow);
        noteColors.add(R.color.light_green);
        noteColors.add(R.color.purple);
        noteColors.add(R.color.sky_blue);
        noteColors.add(R.color.white);
        noteColors.add(R.color.teal_200);

        Random random = new Random();
        int number = random.nextInt(noteColors.size());

        return noteColors.get(number);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView noteTitle,noteContent;
        LinearLayout noteLayout;

        public NoteViewHolder(@NonNull View itemView) {

            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteContent = itemView.findViewById(R.id.note_content);
            noteLayout = itemView.findViewById(R.id.linear_layout1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.logout){

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart() {

        super.onStart();
        fireAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(fireAdapter != null){

            fireAdapter.stopListening();

        }

    }

}