package com.example.eschat_final_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.eschat_final_firebase.databinding.ActivityEditarMyProfileBinding;
import com.example.eschat_final_firebase.databinding.ActivityPesquisarBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Activity_Pesquisar extends AppCompatActivity {

    private ActivityPesquisarBinding binding;
    RecyclerView recyclerView;
    ArrayList<PesqUser> pesqUsersArrayList;
    PesqAdapter pesqAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityPesquisarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Feching data...");
        progressDialog.show();


        recyclerView=findViewById(R.id.rcwpesquisar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        pesqUsersArrayList=new ArrayList<PesqUser>();
        pesqAdapter=new PesqAdapter(Activity_Pesquisar.this,pesqUsersArrayList);

        recyclerView.setAdapter(pesqAdapter);

        EventChangedListener();

       binding.editpesquisarusers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String valorPesquisa=binding.editpesquisarusers.getText().toString().trim();
                pesqAdapter.getFilter().filter(valorPesquisa);

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }

    private void EventChangedListener() {
        db.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error!=null)
                        {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc:value.getDocumentChanges())
                        {
                            if (dc.getType() == DocumentChange.Type.ADDED)
                            {
                                pesqUsersArrayList.add(dc.getDocument().toObject(PesqUser.class));
                            }

                            pesqAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }



                    }
                });

    }

}