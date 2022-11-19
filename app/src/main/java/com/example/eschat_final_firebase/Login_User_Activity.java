package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.ActivityLoginUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login_User_Activity extends AppCompatActivity {

    private ActivityLoginUserBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean userExist = false;
    Configuration config;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.txtcriarconta.setOnClickListener(view ->
        {
            startActivity(new Intent(this, Register_User_Activity.class));
            finish();
        });

        binding.txtrecuperarconta.setOnClickListener(view -> startActivity(new Intent(this, Repor_Pass_Activity.class)));

        binding.btnlogin.setOnClickListener(view ->
        {
            VerificarDadosLogin();
            userExist = false;
        });
    }

    private void VerificarDadosLogin() {
        String email = binding.editemaillogin.getText().toString().trim();
        String senha = binding.editpasslogin.getText().toString().trim();
        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                VerificarLoginFireBase(email, senha);
            } else {
                Toast.makeText(this, "Preencha a senha ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha o seu Email", Toast.LENGTH_SHORT).show();
        }
    }

    private void VerificarLoginFireBase(String email, String senha) {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("email").equals(email) && document.getString("senha").equals(senha)) {
                                    Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();

                                    id=document.getId();
                                    userExist = true;
                                    hideKeyboard(Login_User_Activity.this);
                                    binding.pbLogin.setVisibility(View.VISIBLE);
                                    finish();
                                    Intent intent=new Intent(getApplicationContext(),Principal_Activity.class);
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                    break;
                                }
                            }
                            if (userExist == false) {
                                Toast.makeText(getApplicationContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                                userExist = false;
                            }
                        } else {
                            binding.pbLogin.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //serve para ocultar o teclado quando clicao no botao de login
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




        @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                binding.txtESChat2.setTextColor(Color.BLACK);
                binding.backgroundlogin.setBackground(getDrawable(R.drawable.colorwhite));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                binding.txtESChat2.setTextColor(Color.WHITE);
                binding.backgroundlogin.setBackground(getDrawable(R.drawable.colorblack));

                break;
        }
    }
}