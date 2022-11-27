package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.ActivityContinuarRegistroBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Continuar_Registro_Activity extends AppCompatActivity {

    ActivityContinuarRegistroBinding binding;
    String email;
    String pass;
    String nomeUtilizador;
    String nomeCompleto;
    String fotoemString;
    String biografia;
    String dataCompleta;
    String fotoUriemString;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int CAMERA_ACTION_CODE=1;
    Bitmap finalphoto;
    byte[] fotoembyte;
    int dia=0;
    int mes=0;
    int ano=0;
    private Uri mSelectedUri;
    String encoded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContinuarRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        email=getIntent().getStringExtra("email");
        pass=getIntent().getStringExtra("senha");
        nomeCompleto=getIntent().getStringExtra("nomeCompleto");
        nomeUtilizador=getIntent().getStringExtra("nomeUtilizador");

        binding.btncontinuarregistro.setOnClickListener(view -> criarContaFireBase(email,pass,nomeCompleto,nomeUtilizador));


       binding.imgfotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Alerte Dialog para saber se quer abrir a camara ou a galeria
                AlertDialog.Builder builder = new AlertDialog.Builder(Continuar_Registro_Activity.this);
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
            }
        });
    }
    private void criarContaFireBase(String email,String senha,String nomeCompleto,String nomeUtilizador)
    {

        //pegar valor da data e da biografia
        biografia=binding.editbiografia.getText().toString().trim();
        dia=binding.dateaniversario.getDayOfMonth();
        mes=binding.dateaniversario.getMonth();
        ano=binding.dateaniversario.getYear();
        dataCompleta= dia+"/"+(mes+1)+"/"+ano;

        //criar na firebase o novo utilizador
     Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("senha", senha);
        user.put("nome_Completo", nomeCompleto);
        user.put("nome_Utilizador",nomeUtilizador);
        user.put("foto_Bitmap_Utilizador",fotoemString);
        user.put("foto_uri_utilizador",encoded);
        user.put("biografia",biografia);
        user.put("data_de_Nascimento",dataCompleta);
        user.put("admin",false);

        db.collection("user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Conta Criada com sucesso",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),Login_User_Activity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
                    }
                })

        ;}

    @Override
    protected void  onActivityResult(int requestCode,int resultCode,@Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAMERA_ACTION_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            Bundle bundle=data.getExtras();
            finalphoto= (Bitmap) bundle.get("data");
            binding.imgfotoperfil.setImageBitmap(finalphoto);
            //Converter a imagem de Bitmap para String
            ByteArrayOutputStream streamDaFotoEmBytes=new ByteArrayOutputStream();
            finalphoto.compress(Bitmap.CompressFormat.PNG,70,streamDaFotoEmBytes);
            fotoembyte=streamDaFotoEmBytes.toByteArray();
            fotoemString= Base64.encodeToString(fotoembyte,Base64.DEFAULT);

        }
        if (requestCode==0)
        {
            //pega o uri da imagem e converte para String
            /*mSelectedUri = data.getData();
            binding.imgfotoperfil.setImageURI(mSelectedUri);
            fotoUriemString=mSelectedUri.toString();*/


            //Converte Uri para BitMap e depois para String
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
             encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
             binding.imgfotoperfil.setImageBitmap(bitmap);
        }
    }



}