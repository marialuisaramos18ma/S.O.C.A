package com.example.projetoauto.fragment.empresa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.activity.empresa.EmpresaEnderecosActivity;
import com.example.projetoauto.activity.empresa.EmpresaPerfilActivity;
import com.example.projetoauto.activity.usuario.UsuarioHomeActivity;
import com.example.projetoauto.helper.FirebaseHelper;
import com.squareup.picasso.Picasso;

public class EmpresaPerfilFragment extends Fragment {

    private ImageView img_logo;

    private TextView text_empresa;

    private LinearLayout menu_perfil_empresa;

    private LinearLayout menu_enderecos;

    private LinearLayout menu_deslogar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_empresa_perfil, container, false);

        iniciaComponentes(view);

        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        configAcesso();

    }

    private void configAcesso() {
        Picasso.get().load(FirebaseHelper.getAuth().getCurrentUser().getPhotoUrl()).into(img_logo);
        text_empresa.setText(FirebaseHelper.getAuth().getCurrentUser().getDisplayName());
    }

    private void configCliques() {
        menu_perfil_empresa.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaPerfilActivity.class)));
        menu_enderecos.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EmpresaEnderecosActivity.class)));
        menu_deslogar.setOnClickListener(v -> deslogar());

    }

    private void deslogar() {
        FirebaseHelper.getAuth().signOut();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), UsuarioHomeActivity.class));
    }


    private void iniciaComponentes(View view) {

        img_logo = view.findViewById(R.id.img_logo);
        text_empresa = view.findViewById(R.id.text_empresa);
        menu_perfil_empresa = view.findViewById(R.id.menu_perfil_empresa);
        menu_enderecos = view.findViewById(R.id.menu_enderecos);
        menu_deslogar = view.findViewById(R.id.menu_deslogar);


    }
}