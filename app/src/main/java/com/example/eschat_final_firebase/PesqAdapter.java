package com.example.eschat_final_firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PesqAdapter extends RecyclerView.Adapter<PesqAdapter.PesqViewHolder> {

    Context context;
    ArrayList<PesqUser>  pesqUserArrayList;

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

        PesqUser pesqUser=pesqUserArrayList.get(position);
        holder.userName.setText(pesqUser.nome_Utilizador);
        holder.name.setText(pesqUser.nome_Completo);

    }

    @Override
    public int getItemCount() {
        return pesqUserArrayList.size();
    }

    public static class PesqViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName;
        TextView name;
        ImageView fotoUser;

        public PesqViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.txtNomeUtilizadorPesq);
            name=itemView.findViewById(R.id.txtNomePesq);
            fotoUser=itemView.findViewById(R.id.imgFotoPerfilPesq);
        }
    }
}
