package com.example.eschat_final_firebase;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eschat_final_firebase.databinding.FragmentMyProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileMyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileMyFragment extends Fragment {

    private FragmentMyProfileBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String fotoEmString;
    private static final int LOCATION_REQUEST = 222;
    Uri uri;
    boolean jacarregou=false;
    int i=0;
    public boolean jaleutudo=false;




    public ProfileMyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileMyFragment newInstance(String param1, String param2) {
        ProfileMyFragment fragment = new ProfileMyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMyProfileBinding.inflate(inflater, container, false);
        id=this.getArguments().getString("USER_ID");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Todo o codigo do fragmento vai ficar aqui
        carregarCaixasTexto();



        binding.btnEditarMyProfile.setOnClickListener(view1 ->
        {
            Intent intent=new Intent(getActivity(),EditarMyProfileActivity.class);
            intent.putExtra("id",id);
            startActivityForResult(intent,1000);
        });
    }

    Bitmap converterStringToBitMap(String fotoEmString)
    {
        byte[] bytes= Base64.decode(fotoEmString,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
        public void carregarCaixasTexto()
        {
            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getId().equals(id))
                                    {
                                        fotoEmString=document.getString("foto_Bitmap_Utilizador");
                                        binding.txtBiografiaMyProfile.setText(document.getString("biografia"));
                                        binding.txtNomeUserMyProfile.setText(document.getString("nome_Utilizador"));
                                        if (fotoEmString.equals("none"))
                                        {
                                            binding.imgfotoMyperfil.setImageResource(R.drawable.foto_sem_nada);
                                        }
                                        else
                                        {
                                            binding.imgfotoMyperfil.setImageBitmap(converterStringToBitMap(fotoEmString));

                                        }
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String fotouser=data.getStringExtra("fotoUser");
        String biografia=data.getStringExtra("biografia");
        String nomeuser=data.getStringExtra("nomeUser");



        if(requestCode==1000)
        {
            binding.imgfotoMyperfil.setImageBitmap(converterStringToBitMap(fotouser));
            binding.txtNomeUserMyProfile.setText(nomeuser);
            binding.txtBiografiaMyProfile.setText(biografia);

        }

    }

}












    //Converter String para uri
   /* Uri converterStringToUri(String fotoEmUri)
    {
        uri = Uri.parse(fotoEmUri);
        return uri;
    }*/






