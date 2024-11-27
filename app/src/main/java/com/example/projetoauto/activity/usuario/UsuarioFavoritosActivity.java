package com.example.projetoauto.activity.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.AdapterListaAutomovel;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Automovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioFavoritosActivity extends AppCompatActivity implements AdapterListaAutomovel.OnclickListener {

    private AdapterListaAutomovel adapterListaAutomovel;

    private final List<Automovel> automovelList = new ArrayList<>();

    private RecyclerView rv_automoveis;

    private ProgressBar progressBar;

    private TextView text_info;

    private List<String> favoritosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_favoritos);

        iniciaComponentes();

        configRv();

        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaFavoritos();
    }

    private void configRv() {
        rv_automoveis.setLayoutManager(new LinearLayoutManager(this));
        rv_automoveis.setHasFixedSize(true);
        adapterListaAutomovel = new AdapterListaAutomovel(automovelList, this);
        rv_automoveis.setAdapter(adapterListaAutomovel);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoritosList.clear();
                    automovelList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoritosList.add(ds.getValue(String.class));
                    }
                    if (favoritosList.size() > 0) {
                        recuperaAutomoveis();
                    } else {
                        text_info.setText("Nenhum Automovel Favoritado");
                        progressBar.setVisibility(View.GONE);
                        adapterListaAutomovel.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void recuperaAutomoveis() {
        for (int i = 0; i < favoritosList.size(); i++) {
            DatabaseReference automoveisPublicosRef = FirebaseHelper.getDatabaseReference()
                    .child("automoveis_publicos")
                    .child(favoritosList.get(i));
            automoveisPublicosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Automovel automovel = snapshot.getValue(Automovel.class);
                    automovelList.add(automovel);

                    if (automovelList.size() == favoritosList.size()) {
                        text_info.setText("");
                        Collections.reverse(automovelList);
                        adapterListaAutomovel.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Favoritos");

        rv_automoveis = findViewById(R.id.rv_automoveis);
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
    }

    @Override
    public void OnClick(Automovel automovel) {
        Intent intent = new Intent(this, UsuarioDetalheAutomovelActivity.class);
        intent.putExtra("automovelSelecionado", automovel);
        startActivity(intent);
    }
}