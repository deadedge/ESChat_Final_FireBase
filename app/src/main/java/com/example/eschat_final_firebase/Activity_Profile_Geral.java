package com.example.eschat_final_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eschat_final_firebase.databinding.ActivityEditarMyProfileBinding;
import com.example.eschat_final_firebase.databinding.ActivityProfileGeralBinding;

public class Activity_Profile_Geral extends AppCompatActivity {

    ActivityProfileGeralBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityProfileGeralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}