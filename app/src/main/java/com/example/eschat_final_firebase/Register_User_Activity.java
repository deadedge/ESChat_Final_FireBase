package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.ActivityLoginUserBinding;
import com.example.eschat_final_firebase.databinding.ActivityRegisterUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register_User_Activity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean jaUtilizado=false;
    String senhaEncriptada="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnregister.setOnClickListener(view ->
        {
            verificardados();
            jaUtilizado=false;

        } );
    }

    private void verificardados()
    {
        String email= binding.editemail.getText().toString().trim();
        String senha= binding.editpass.getText().toString().trim();
        String nomeCompleto= binding.editnomecompleto.getText().toString().trim();
        String nomeUtilizador= binding.editnomeuser.getText().toString().trim();

        if(!email.isEmpty())
        {
            if (!senha.isEmpty())
            {
                if (!nomeCompleto.isEmpty())
                {
                    if (!nomeUtilizador.isEmpty())
                    {
                        if(senha.length()>=6)
                        {
                            //se nao tiver nenhum user criado vai dar bug e nao vai avancar
                            verificarContaFirebase(email,senha,nomeCompleto,nomeUtilizador);
                        }
                        else
                        {
                            Toast.makeText(this,"A senha tem de ter no minimo 6 caracters",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Preencha o seu Nome de Utilizador ",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this,"Preencha o seu Nome Completo",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this,"Preencha a sua senha",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"Preencha o seu email",Toast.LENGTH_SHORT).show();
        }
    }
    private void criarComCriacaoContaFireBase(String email,String senha,String nomeCompleto,String nomeUtilizador)
    {
        try {
            senhaEncriptada=Encriptar_Pass.encrypt(senha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent=new Intent(getApplicationContext(),Continuar_Registro_Activity.class);
        intent.putExtra("email",email);
        intent.putExtra("senha",senhaEncriptada);
        intent.putExtra("nomeCompleto",nomeCompleto);
        intent.putExtra("nomeUtilizador",nomeUtilizador);
        startActivity(intent);
        finish();
    }

    private void verificarContaFirebase(String email,String senha,String nomeCompleto,String nomeUtilizador)
    {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               if(document.getString("email").equals(email)||document.getString("nome_Utilizador").equals(nomeUtilizador))
                               {
                                   if(document.getString("email").equals(email))
                                   {
                                       Toast.makeText(getApplicationContext(),"Email ja usado! Escolha outro",Toast.LENGTH_SHORT).show();
                                       jaUtilizado=true;
                                   }
                                   else if(document.getString("nome_Utilizador").equals(nomeUtilizador))
                                   {
                                       Toast.makeText(getApplicationContext(),"Nome de utilizador ja usado! Escolha outro",Toast.LENGTH_SHORT).show();
                                       jaUtilizado=true;
                                   }
                               }
                            }
                            if (jaUtilizado==false)
                            {
                                criarComCriacaoContaFireBase(email,senha,nomeCompleto,nomeUtilizador);
                                jaUtilizado=false;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                binding.txtESChat.setTextColor(Color.BLACK);
                binding.backgroundregister.setBackground(getDrawable(R.drawable.colorwhite));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                binding.txtESChat.setTextColor(Color.WHITE);
                binding.backgroundregister.setBackground(getDrawable(R.drawable.colorblack));

                break;
        }
    }
}