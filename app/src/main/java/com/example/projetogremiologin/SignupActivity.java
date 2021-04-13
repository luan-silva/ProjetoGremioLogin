package com.example.projetogremiologin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    public FirebaseAuth fAuth;
    public FirebaseUser fUser;
    TextInputEditText email, password, name, phone;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.signupEmailEditText);
        password = findViewById(R.id.editTextSignupPassword);
        name = findViewById(R.id.editTextSignupName);
        phone = findViewById(R.id.editTextSignupPhone);
        signup = findViewById(R.id.btSignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email != null && password != null && name != null && phone != null) {
                    criarUser();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void criarUser() {
        String _email = email.getText().toString();
        String _senha = password.getText().toString();

        fAuth.createUserWithEmailAndPassword(_email, _senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fUser = fAuth.getCurrentUser();
                            salvarUmUsuarioAutenticado();
                            Toast.makeText(getApplicationContext(), "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.v("PDM", "" + task.getException());
                        }
                    }
                });
    }

    private void salvarUmUsuarioAutenticado() {
        DatabaseReference meuJSON = FirebaseDatabase.getInstance().getReference();
        DatabaseReference meuJSONUsuarios = meuJSON.child("usuarios");

        if (fUser != null) {
            meuJSONUsuarios.child(fUser.getUid()).child("nome").setValue(name.getText().toString());
            meuJSONUsuarios.child(fUser.getUid()).child("telefone").setValue(phone.getText().toString());

        } else {
            Log.v("PDM", "sem usuario");
        }
    }
}