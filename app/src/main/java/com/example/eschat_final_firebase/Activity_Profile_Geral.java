package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.ActivityProfileGeralBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity_Profile_Geral extends AppCompatActivity {

    ActivityProfileGeralBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String nomeUtilizador="";
    String nome="";
    String biografia="";
    String fotoUser="";
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityProfileGeralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nomeUtilizador= getIntent().getStringExtra("userName");
        pegarDados();
        binding.txtNomeUtilizadorPerfilGeral.setText(nomeUtilizador);




    }
    public void pegarDados()
    {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                if (document.getString("nome_Utilizador").equals(nomeUtilizador))
                                {
                                    nome= document.getString("nome_Completo");
                                    biografia=document.getString("biografia");
                                    fotoUser=document.getString("foto_Bitmap_Utilizador");
                                    binding.txtBiografiaPerfilGeral.setText(biografia);
                                    binding.imgfotoPerfilGeral.setImageBitmap(converterStingToBitmap(fotoUser));
                                    binding.txtnomePerfilGeral.setText(nome);
                                }

                            }
                        } else {
                        Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });









    }

    Bitmap converterStingToBitmap(String imagememstring)
    {

        byte[] bytes= Base64.decode(imagememstring,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}