package com.example.projetoauto.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.activity.usuario.UsuarioFavoritosActivity;
import com.example.projetoauto.activity.usuario.UsuarioHomeActivity;
import com.example.projetoauto.activity.usuario.UsuarioPerfilActivity;
import com.example.projetoauto.auth.CriarContaActivity;
import com.example.projetoauto.auth.LoginActivity;
import com.example.projetoauto.helper.FirebaseHelper;

public class UsuarioPerfilFragment extends Fragment {

    private ConstraintLayout constraint_logado;

    private ConstraintLayout constraint_deslogado;

    private LinearLayout menu_perfil;

    private LinearLayout menu_favoritos;

    private LinearLayout menu_deslogar;

    private Button btn_entrar;

    private Button btn_cadastrar;

    private TextView text_usuario;


    //onCreateView chama so uma vez
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario_perfil, container, false);

        iniciaComponetes(view);

        configCliques();

        return view;
    }


    //onStart sempre que acessar chama o metodo
    @Override
    public void onStart() {
        super.onStart();

        verificaAcesso();
    }

    private void verificaAcesso() {
        if (FirebaseHelper.getAutenticado()) {
            constraint_deslogado.setVisibility(View.GONE);
            constraint_logado.setVisibility(View.VISIBLE);
            menu_deslogar.setVisibility(View.VISIBLE);
            menu_perfil.setVisibility(View.VISIBLE);
            menu_favoritos.setVisibility(View.VISIBLE);
            text_usuario.setText(FirebaseHelper.getAuth().getCurrentUser().getDisplayName());
        } else {
            constraint_deslogado.setVisibility(View.VISIBLE);
            constraint_logado.setVisibility(View.GONE);
            menu_deslogar.setVisibility(View.GONE);
            menu_perfil.setVisibility(View.GONE);
            menu_favoritos.setVisibility(View.GONE);

        }

    }

    private void configCliques() {
        btn_entrar.setOnClickListener(v -> startActivity(new Intent(requireActivity(), LoginActivity.class)));
        btn_cadastrar.setOnClickListener(v -> startActivity(new Intent(requireActivity(), CriarContaActivity.class)));

        menu_deslogar.setOnClickListener(v -> deslogar());
        menu_perfil.setOnClickListener(V -> startActivity(new Intent(requireActivity(), UsuarioPerfilActivity.class)));
        menu_favoritos.setOnClickListener(V -> startActivity(new Intent(requireActivity(), UsuarioFavoritosActivity.class)));
    }

    private void deslogar() {
        FirebaseHelper.getAuth().signOut();
        requireActivity().finish();
        startActivity(new Intent(requireActivity(), UsuarioHomeActivity.class));
    }

    private void iniciaComponetes(View view) {
        constraint_logado = view.findViewById(R.id.constraint_logado);
        constraint_deslogado = view.findViewById(R.id.constraint_deslogado);
        menu_perfil = view.findViewById(R.id.menu_perfil);
        menu_favoritos = view.findViewById(R.id.menu_favoritos);
        menu_deslogar = view.findViewById(R.id.menu_deslogar);
        btn_entrar = view.findViewById(R.id.btn_entrar);
        btn_cadastrar = view.findViewById(R.id.btn_cadastrar);
        text_usuario = view.findViewById(R.id.text_usuario);
    }
}