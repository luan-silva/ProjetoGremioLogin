package com.example.projetogremiologin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    public FirebaseAuth fAuth;
    public FirebaseUser fUser;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Item> mItems;
    private String anotherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItems = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            anotherUserId = (String) bundle.get("userId");
        }
        manageDatabase();
    }

    private void manageDatabase() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONRoupas = mDatabaseRef.child("items");
        Log.d("FeedActivity", "AAAAAAAAAAAAAAAAAAAA" + anotherUserId);
        DatabaseReference JSONRoupasDoUser = JSONRoupas.child(anotherUserId != null ? anotherUserId : fUser.getUid());

        JSONRoupasDoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("PDM", "Número de roupas:" + snapshot.getChildrenCount());
                Item item = null;
                for (DataSnapshot cadaRoupa : snapshot.getChildren()) {
                    item = cadaRoupa.getValue(Item.class);
                    mItems.add(item);
                    Log.v("PDM", "Preço da roupa:" + item.getPreco());
                    Log.v("PDM", "Path da Foto:" + item.getPathFoto());
                }

                mAdapter = new ImageAdapter(FeedActivity.this, mItems);
                mRecyclerView.setAdapter(mAdapter);

//                if (item != null) {
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                    StorageReference pasta = storageReference.child("imagens");
//                    StorageReference fotoItem = pasta.child(item.getPathFoto() + ".jpeg");
//// Download directly from StorageReference using Glide
//// (See MyAppGlideModule for Loader registration)
////                    GlideApp.with(getApplicationContext())
////                            .load(fotoItem)
////                            .into(im);
//                    GlideApp.with(getApplicationContext())
//                            .load(fotoItem)
//                            .into(im);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                ;
            }
        });
    }
}