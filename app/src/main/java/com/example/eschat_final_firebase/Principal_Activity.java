package com.example.eschat_final_firebase;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}