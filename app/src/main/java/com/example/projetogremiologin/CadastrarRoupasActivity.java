package com.example.projetogremiologin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CadastrarRoupasActivity extends AppCompatActivity {
    public FirebaseAuth fAuth;
    public FirebaseUser fUser;

    ProgressBar mProgressBar;
    TextInputEditText etPreco, boughtPrice, category, description;
    ImageView im;
    Button uploadImage;
    Button feedAcitivtyButton;
    Button shareClothes;
    UploadTask ut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_roupas);
        im = findViewById(R.id.imageView);
        mProgressBar = findViewById(R.id.cadastroProgressBar);
        uploadImage = findViewById(R.id.btEnviar);
        feedAcitivtyButton = findViewById(R.id.buttonFeedActivity);
        shareClothes = findViewById(R.id.share_clothes);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        etPreco = findViewById(R.id.edtPreco);
        boughtPrice = findViewById(R.id.bough_price_editText);
        category = findViewById(R.id.category_editText);
        description = findViewById(R.id.description_editText);


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ut != null && ut.isInProgress()) {
                    Toast.makeText(CadastrarRoupasActivity.this, "Upload em progresso", Toast.LENGTH_SHORT).show();
                } else {
                    mandarImagem();
                }
            }
        });

        feedAcitivtyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFeedActivity();
            }
        });

        shareClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateDynamicLink();
            }
        });
    }

    private void generateDynamicLink() {
        String deeplink = "https%3A%2F%2Fwww.example.com%2F%3Fuid%3D" + fAuth.getUid();
        String myDynamicLink = "https://projetogremiologin.page.link/?link="
                + deeplink
                + "&apn=com.example.projetogremiologin";

        Log.d("AAAAAAAAAAAA", myDynamicLink);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share content");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, myDynamicLink);
        startActivity(Intent.createChooser(intent, "Compartilhar"));
    }


//    public void salvarItem(View v) {
//        mandarImagem();
//
//    }
//
//    public void abrirCamera(View v) {
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(i, 1);
//    }
//
//    public void abrirGaleria(View v) {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//
//        if (intent.resolveActivity(this.getPackageManager()) != null) {
//            startActivityForResult(intent, 2);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int codeResult, Intent data) {
        super.onActivityResult(requestCode, codeResult, data);

        if (requestCode == 1 && codeResult == RESULT_OK) {
            Bitmap imagem = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(imagem);
        }

        if (requestCode == 4 && codeResult == RESULT_OK) {
            Uri pathImg = data.getData();

            Bitmap imagem;

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    imagem = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), pathImg));
                    im.setImageBitmap(imagem);
                } else {
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), pathImg);
                    im.setImageBitmap(imagem);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }


        if (requestCode == 2 && codeResult == RESULT_OK) {
            Uri pathImg = data.getData();
            Bitmap imagem;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    imagem = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), pathImg));
                    im.setImageBitmap(imagem);
                } else {
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), pathImg);
                    im.setImageBitmap(imagem);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void abrirGaleria2(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        startActivityForResult(intent, 4);

    }


//    public void cadastrarRoupa(View v) {
//        mandarImagem();
//    }


    public void mandarImagem() {
        Log.v("PDM", "Pintando imagem");


        Bitmap bitmap = null;

        try {
            bitmap = Bitmap.createBitmap(im.getWidth(), im.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            im.draw(canvas);
            Log.v("PDM", "com Draw");

        } catch (Exception e) {
            BitmapDrawable drawable = (BitmapDrawable) im.getDrawable();
            bitmap = drawable.getBitmap();
            Log.v("PDM", "sem Draw");
        }
        Log.v("PDM", "tamanho:" + bitmap.getByteCount());


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pasta = storageReference.child("imagens");

        final String randomName = UUID.randomUUID().toString();
        StorageReference foto = pasta.child(randomName + ".jpeg");

        ByteArrayOutputStream dados = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, dados);


        ut = foto.putBytes(dados.toByteArray());

        ut.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.v("PDM", "Sucesso no envio");
                salvarItemNoBanco(randomName);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }, 500);


                //completeEnvioDeRoupa(randomName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(
                        CadastrarRoupasActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listarItemsDoUser() {
//        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference JSONRoupas = banco.child("items");
//        DatabaseReference JSONRoupasDoUser = JSONRoupas.child(fUser.getUid());
//
//        JSONRoupasDoUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.v("PDM", "Número de roupas:" + snapshot.getChildrenCount());
//                Item item = null;
//                for (DataSnapshot cadaRoupa : snapshot.getChildren()) {
//                    item = cadaRoupa.getValue(Item.class);
//                    Log.v("PDM", "Preço da roupa:" + item.getPreco());
//                    Log.v("PDM", "Path da Foto:" + item.getPathFoto());
//                }
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
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void goToFeedActivity() {
        Intent feedActivity = new Intent(this, FeedActivity.class);
        startActivity(feedActivity);
    }


    private void salvarItemNoBanco(String fotoPath) {
        Log.v("PDM", "Imagem Enviada, Salvando Item");
        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONRoupas = banco.child("items");
        DatabaseReference JSONRoupasDoUser = JSONRoupas.child(fUser.getUid());

        String key = JSONRoupasDoUser.push().getKey();

        Double preco = Double.parseDouble(etPreco.getText().toString());
        Double boughPrice = Double.parseDouble(boughtPrice.getText().toString());
        Item item = new Item(
                fotoPath,
                preco,
                boughPrice,
                category.getText().toString(),
                description.getText().toString());

        JSONRoupasDoUser.child(key).setValue(item);
    }

    public void listarRoupas() {
        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONRoupas = banco.child("roupas");
        DatabaseReference JSONRoupasDoUser = JSONRoupas.child(fUser.getUid());

        JSONRoupasDoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Roupa roupa = ds.getValue(Roupa.class);
                    System.out.println(snapshot.getKey() + " tem preço " + roupa.getPreco());
                    System.out.println(snapshot.getKey() + " tem foto " + roupa.getFoto());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void completeEnvioDeRoupa(String idFotoRoupa) {
        Log.v("PDM", "Enviar a nova roupa");
        DatabaseReference banco = FirebaseDatabase.getInstance().getReference();
        DatabaseReference JSONRoupas = banco.child("roupas");
        DatabaseReference JSONRoupasDoUser = JSONRoupas.child(fUser.getUid());
        String key = JSONRoupasDoUser.push().getKey();
        Roupa roupa = new Roupa(idFotoRoupa, Double.parseDouble(etPreco.getText().toString()));
        JSONRoupasDoUser.child(key).setValue(roupa);


    }

    public void baixarImagem(View v) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pasta = storageReference.child("imagens");
        StorageReference eu = pasta.child("eu.jpg");
// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        GlideApp.with(this /* context */)
                .load(eu)
                .into(im);


    }

}