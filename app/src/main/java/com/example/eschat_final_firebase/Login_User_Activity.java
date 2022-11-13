package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private ActivityLoginUserBinding  binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean userExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




       binding.txtcriarconta.setOnClickListener(view -> startActivity(new Intent(this,Register_User_Activity.class)));

       binding.txtrecuperarconta.setOnClickListener(view -> startActivity(new Intent(this,Repor_Pass_Activity.class)));

       binding.btnlogin.setOnClickListener(view ->
       {
           VerificarDadosLogin();
           userExist=false;
       });
    }
    private void VerificarDadosLogin()
    {
        String email=binding.editemaillogin.getText().toString().trim();
        String senha=binding.editpasslogin.getText().toString().trim();
        if (!email.isEmpty())
        {
            if (!senha.isEmpty())
            {
              VerificarLoginFireBase(email,senha);
            }
            else
            {
                Toast.makeText(this,"Preencha a senha ",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"Preencha o seu Email",Toast.LENGTH_SHORT).show();
        }
    }
    private void VerificarLoginFireBase(String email,String senha)
    {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("email").equals(email) && document.getString("senha").equals(senha))
                                {
                                    Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                                    userExist=true;
                                    hideKeyboard(Login_User_Activity.this);
                                    binding.pbLogin.setVisibility(View.VISIBLE);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),Principal_Activity.class));
                                    break;
                                }
                            }
                            if (userExist==false)
                            {
                                Toast.makeText(getApplicationContext(), "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                                userExist=false;
                            }
                        }
                        else
                        {
                            binding.pbLogin.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //serve para ocultar o teclado
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
}