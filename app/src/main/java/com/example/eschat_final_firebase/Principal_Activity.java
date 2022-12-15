package com.example.eschat_final_firebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.eschat_final_firebase.databinding.ActivityPrincipalBinding;

public class Principal_Activity extends AppCompatActivity {

    ActivityPrincipalBinding binding;
    Bundle bundle;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPrincipalBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        replaceFragment(new MensagensFragment());
        id=getIntent().getStringExtra("id");


        binding.bottomNavigationView.setOnItemSelectedListener(item ->
        {

            switch (item.getItemId())
            {
                case R.id.mensagens:
                    replaceFragment(new MensagensFragment());
                    break;
                case R.id.pesquisa:
                    replaceFragment(new ProcurarFragment());

                    break;
                case R.id.foto:
                    replaceFragment(new AdicionarFotoFragment());

                    break;
                case R.id.profile:
                    replaceFragment(new ProfileMyFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new DefinicoesFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putString("USER_ID",id);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
