package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.ActivityEditarMyProfileBinding;
import com.example.eschat_final_firebase.databinding.ActivityLoginUserBinding;
import com.example.eschat_final_firebase.databinding.ActivityPrincipalBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class EditarMyProfileActivity extends AppCompatActivity {

     private ActivityEditarMyProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id;
    String biografia;
    String nomeuser;
    String nome;
    public static final int CAMERA_ACTION_CODE=1;
    String fotoemString;
    Bitmap finalphoto;
    byte[] fotoembyte;
    String encoded;
    boolean biografiaigual=false;
    boolean nomeuserigual=false;
    boolean nomeigual=false;
    boolean fotoperfiligual=false;
    boolean fotoigual=false;
    String fotoemStringNaoMexe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityEditarMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id=getIntent().getStringExtra("id");
        carregarCampos();





        binding.imgEditMyProfile.setOnClickListener(view ->
        {

            //Alerte Dialog para saber se quer abrir a camara ou a galeria
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarMyProfileActivity.this);
            builder.setTitle("Deseja abrir a galeria ou a camara?");
            builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //abrir a galeria
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 0);
                }
            });
            builder.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // startActivityForResult(intent, 2);
                    //abrir a camara
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!=null)
                    {
                        startActivityForResult(intent,CAMERA_ACTION_CODE);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Esta app nao suporta essa açao",Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
        });
        binding.btnconfirmar.setOnClickListener(view ->
        {
            /*biografia=binding.EditBiografiaProfile.getText().toString().trim();
            nomeuser=binding.Editnomeuse.getText().toString().trim();
            DocumentReference docRef = db.collection("user").document(id);
            docRef.update("biografia",biografia );
            docRef.update("nome_Utilizador",nomeuser);
            docRef.update("foto_Bitmap_Utilizador",fotoemString);*/
            verificarCampos();
            Intent returnIntent = new Intent();
          //  returnIntent.putExtra("fotoUser",fotoemString);
            returnIntent.putExtra("biografia",biografia);
            returnIntent.putExtra("nomeUser",nomeuser);
            returnIntent.putExtra("nome",nome);
            setResult(1000,returnIntent);
            finish();
        });
        binding.btnclose.setOnClickListener(view ->{
            Intent returnIntent = new Intent();
            if (fotoemString != fotoemStringNaoMexe)
            {
                returnIntent.putExtra("fotoUser",fotoemStringNaoMexe);
            }
            else
            {
                returnIntent.putExtra("fotoUser",fotoemString);
            }
           returnIntent.putExtra("biografia",biografia);
            returnIntent.putExtra("nomeUser",nomeuser);
            returnIntent.putExtra("nome",nome);
           setResult(1000,returnIntent);
            finish();

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAMERA_ACTION_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            Bundle bundle=data.getExtras();
            finalphoto= (Bitmap) bundle.get("data");
            binding.imgEditMyProfile.setImageBitmap(finalphoto);
            //Converter a imagem de Bitmap para String
            ByteArrayOutputStream streamDaFotoEmBytes=new ByteArrayOutputStream();
            finalphoto.compress(Bitmap.CompressFormat.PNG,70,streamDaFotoEmBytes);
            fotoembyte=streamDaFotoEmBytes.toByteArray();
            fotoemString= Base64.encodeToString(fotoembyte,Base64.DEFAULT);

        }
        if (requestCode==0)
        {

            if (null != data)
            {
                Uri imageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                //BASE64
                fotoemString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                binding.imgEditMyProfile.setImageBitmap(bitmap);
            }

        }
    }
    public  void verificarCampos()
    {
                biografia=binding.EditBiografiaProfile.getText().toString().trim();
                nomeuser=binding.Editnomeuse.getText().toString().trim();
                nome=binding.Editnome.getText().toString().trim();
                db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(id) && document.getString("biografia").equals(biografia)) {
                                    biografiaigual=true;
                                }
                                else
                                {
                                    biografiaigual=false;
                                }
                                if (document.getId().equals(id) && document.getString("nome_Utilizador").equals(nomeuser)) {
                                    nomeuserigual = true;
                                }
                                else
                                {
                                    nomeuserigual=false;

                                }
                                if (document.getId().equals(id) && document.getString("nome_Completo").equals(nome)) {
                                    nomeigual = true;
                                }
                                else
                                {
                                    nomeigual=false;

                                }
                                if (document.getId().equals(id) && document.getString("foto_Bitmap_Utilizador").equals(nomeuser))
                                {
                                    fotoigual=true;
                                    CarregarDadosNaBaseDeDados();
                                }
                                else
                                {
                                    fotoigual=false;
                                    CarregarDadosNaBaseDeDados();
                                }


                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void carregarCampos()
    {

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(id))
                                {
                                    biografia=document.getString("biografia");
                                    nomeuser=document.getString("nome_Utilizador");
                                    nome=document.getString("nome_Completo");
                                    fotoemString=document.getString("foto_Bitmap_Utilizador");
                                    fotoemStringNaoMexe=document.getString("foto_Bitmap_Utilizador");
                                    binding.imgEditMyProfile.setImageBitmap(converterStringToBitMap(fotoemString));
                                    binding.EditBiografiaProfile.setText(biografia);
                                    binding.Editnomeuse.setText(nomeuser);
                                    binding.Editnome.setText(nome);
                                }
                            }

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void CarregarDadosNaBaseDeDados()
    {

        if (biografiaigual==true)
        {

        }
        else
        {
            DocumentReference docRef = db.collection("user").document(id);
            docRef.update("biografia",biografia );
        }
        if (nomeuserigual==true)
        {

        }
        else
        {
            DocumentReference docRef = db.collection("user").document(id);
            docRef.update("nome_Utilizador",nomeuser );
        }
        if (nomeigual==true)
        {

        }
        else
        {
            DocumentReference docRef = db.collection("user").document(id);
            docRef.update("nome_Completo",nome );
        }
        if (fotoigual==true)
        {

        }
        else
        {
            DocumentReference docRef = db.collection("user").document(id);
            docRef.update("foto_Bitmap_Utilizador",fotoemString );
        }
    }
    Bitmap converterStringToBitMap(String fotoEmString)
    {
        byte[] bytes= Base64.decode(fotoEmString,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }




}

