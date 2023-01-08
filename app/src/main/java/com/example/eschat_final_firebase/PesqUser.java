package com.example.eschat_final_firebase;

import android.graphics.Bitmap;

public class PesqUser {

   String foto_Bitmap_Utilizador;
   String nome_Utilizador;
   String nome_Completo;
   String fotoemString;

   public PesqUser(){}

   public PesqUser(String foto_Bitmap_Utilizador,String nome_Utilizador, String nome_Completo, String fotoemString) {
      this.foto_Bitmap_Utilizador = foto_Bitmap_Utilizador;
      this.nome_Utilizador = nome_Utilizador;
      this.nome_Completo = nome_Completo;
      this.fotoemString = fotoemString;
   }

   public String getFoto_Bitmap_Utilizador() {
      return foto_Bitmap_Utilizador;
   }

   public void setFoto_Bitmap_Utilizador(String foto_Bitmap_Utilizador) {
      this.foto_Bitmap_Utilizador = foto_Bitmap_Utilizador;
   }

   public String getNome_Utilizador() {
      return nome_Utilizador;
   }

   public void setNome_Utilizador(String nome_Utilizador) {
      this.nome_Utilizador = nome_Utilizador;
   }

   public String getNome_Completo() {
      return nome_Completo;
   }

   public void setNome_Completo(String nome_Completo) {
      this.nome_Completo = nome_Completo;
   }

   public String getFotoemString() {
      return fotoemString;
   }

   public void setFotoemString(String fotoemString) {
      this.fotoemString = fotoemString;
   }
}
