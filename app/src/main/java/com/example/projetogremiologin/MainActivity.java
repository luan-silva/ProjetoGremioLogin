package com.example.projetogremiologin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    private String anotherUserId;

    EditText etEmail, etSenha;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.editTextemail);
        etSenha = findViewById(R.id.editTextPassword);
        signupText = findViewById(R.id.signup_text);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        checkForDynamicLinks();

        //etNome= findViewById(R.id.edtNome);
        //lerUmUsuario();
        // salvarUmUsuario();
        // modificaUsuario("0020");
    }

    void checkForDynamicLinks() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Log.d("FEEDACIVITY", "i got a deeplinkKKKKKKKKKKKKKKKK");
                Uri deeplink = null;
                if (pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.getLink();
                }

                if (deeplink != null) {
                    Log.d("FEEDACIVITY", "i got a deeplinkAAAAAAAA\n :" + deeplink.toString());
                    anotherUserId = deeplink.getQueryParameter("uid");
                    goToFeedActivity(anotherUserId);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FeedActivity", "Error, couldn't retrieve dynamic link data");
            }
        });
    }

    private void goToFeedActivity(String anotherUserId) {
        Intent intent = new Intent(this, FeedActivity.class);
        intent.putExtra("userId", anotherUserId);
        startActivity(intent);
    }


//    private void modificaUsuario(String s) {
//        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference meuJSONUsuarios = meuJSON.child("usuarios");
//        meuJSONUsuarios.child(s).child("nome").setValue("Windson");
//    }
//
//
//    private void salvarUmUsuario() {
//        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference meuJSONUsuarios = meuJSON.child("usuarios");
//
//        String key = meuJSONUsuarios.push().getKey();
//        Usuario u = new Usuario("Filha do Renato", "Gaucho", "+552188888888", key);
//
//        meuJSONUsuarios.child(key).setValue(u);
//    }


    private void lerUmUsuario() {
        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
        DatabaseReference meuJSONUsuarios = meuJSON.child("usuarios").child("0001");
        DatabaseReference meuJSONItens = meuJSON.child("itens");

        meuJSONUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("PDM", "Nome do 0001:" + snapshot.child("nome").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


//    private void lerUsuarios() {
//        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference meuJSONUsuarios = meuJSON.child("usuarios");
//        DatabaseReference meuJSONItens = meuJSON.child("itens");
//
//        meuJSONUsuarios.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.v("PDM", "Lista de Usuarios:" + snapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    public void redefinirSenha(View v) {
//        // FirebaseUser user = fAuth.getCurrentUser();
//        //user.updatePassword()
//
//        fAuth.sendPasswordResetEmail("windson@virtual.ufc.br");
//    }


    public void checarLoginESenha(View v) {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();


        fAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fUser = fAuth.getCurrentUser();
                            Log.v("PDM", fUser.getUid());
                            Log.v("PDM", "" + fUser.isEmailVerified());
                            if (!fUser.isEmailVerified()) {
                                fUser.sendEmailVerification();
                            }
                            Intent intent = new Intent(getApplicationContext(), CadastrarRoupasActivity.class);
                            startActivity(intent);

                        } else //senha errada
                        {
                            Toast.makeText(getApplicationContext(), "Deu Errado o Login", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}