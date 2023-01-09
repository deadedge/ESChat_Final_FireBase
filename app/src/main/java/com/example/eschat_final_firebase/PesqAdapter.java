package com.example.eschat_final_firebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;

public class PesqAdapter extends RecyclerView.Adapter<PesqAdapter.PesqViewHolder> implements Filterable {

    Context context;
    ArrayList<PesqUser>  pesqUserArrayList;
    String imagemUserString;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    PesqUser pesqUser;
    String nomeUtilizador;
    String nome;
    String id="";


    public PesqAdapter(Context context, ArrayList<PesqUser> pesqUserArrayList) {
        this.context = context;
        this.pesqUserArrayList = pesqUserArrayList;
    }

    @NonNull
    @Override
    public PesqAdapter.PesqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rcwlayoutpesq,parent,false);
        return new PesqViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull PesqAdapter.PesqViewHolder holder, int position) {

            pesqUser=pesqUserArrayList.get(position);
            imagemUserString=pesqUser.foto_Bitmap_Utilizador;
            holder.userName.setText(pesqUser.nome_Utilizador);
            holder.name.setText(pesqUser.nome_Completo);
            holder.fotoUser.setImageBitmap(converterStingToBitmap(imagemUserString));
            holder.cardViewPesq.setOnClickListener(view -> {
            nomeUtilizador=holder.userName.getText().toString().trim();
            Intent intent=new Intent(view.getContext(),Activity_Profile_Geral.class);
            intent.putExtra("userName",nomeUtilizador);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return pesqUserArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }


    private final Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<PesqUser> filteredNewsList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){

                filteredNewsList.addAll(pesqUserArrayList);

            }else {

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PesqUser utilizador : pesqUserArrayList){

                    if (utilizador.getNome_Utilizador().toLowerCase().contains(filterPattern))
                        filteredNewsList.add(utilizador);

                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredNewsList;
            results.count = filteredNewsList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            pesqUserArrayList.clear();
            pesqUserArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



    public static class PesqViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName;
        TextView name;
        ImageView fotoUser;
        LinearLayout cardViewPesq;

        public PesqViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.txtNomeUtilizadorPesq);
            name=itemView.findViewById(R.id.txtNomePesq);
            fotoUser=itemView.findViewById(R.id.imgFotoPerfilPesq);
            cardViewPesq=itemView.findViewById(R.id.cardviewPesq);


        }
    }

    Bitmap converterStingToBitmap(String imagememstring)
    {

        byte[] bytes= Base64.decode(imagememstring,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }



}
