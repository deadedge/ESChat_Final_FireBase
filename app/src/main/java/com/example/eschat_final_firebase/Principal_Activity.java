package com.example.eschat_final_firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.eschat_final_firebase.databinding.ActivityPrincipalBinding;

public class Principal_Activity extends AppCompatActivity {

    ActivityPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPrincipalBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        replaceFragment(new MensagensFragment());

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


            }




            return true;
        });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}