package com.example.projetoauto.activity.empresa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.AdapterSelecionaEndereco;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpresaSelecionaEnderecoActivity extends AppCompatActivity implements AdapterSelecionaEndereco.OnClickListener {

    private AdapterSelecionaEndereco adapterSelecionaEndereco;
    private final List<Endereco> enderecoList = new ArrayList<>();
    private RecyclerView rv_enderecos;
    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_seleciona_endereco);

        iniciarComponentes();
        configCliques();
        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaEndereco();
    }

    private void configRv() {
        rv_enderecos.setLayoutManager(new LinearLayoutManager(this));
        rv_enderecos.setHasFixedSize(true);
        adapterSelecionaEndereco = new AdapterSelecionaEndereco(enderecoList, this);
        rv_enderecos.setAdapter(adapterSelecionaEndereco);
    }

    private void recuperaEndereco() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());

        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    enderecoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        endereco = ds.getValue(Endereco.class);
                        enderecoList.add(endereco);
                    }
                } else {
                }
                Collections.reverse(enderecoList);
                adapterSelecionaEndereco.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_adicionar).setOnClickListener(v -> startActivity(new Intent(this, EmpresaFormEnderecoActivity.class)));

    }

    private void iniciarComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Meus Endereços");
        rv_enderecos = findViewById(R.id.rv_enderecos);
    }


    public void OnClick(Endereco endereco) {
        Intent intent = new Intent();
        intent.putExtra("enderecoSelecionado", endereco);
        setResult(RESULT_OK, intent);
        finish();
    }
}