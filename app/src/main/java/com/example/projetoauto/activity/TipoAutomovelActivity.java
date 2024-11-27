package com.example.projetoauto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.AdapterTipo;
import com.example.projetoauto.helper.TipoList;
import com.example.projetoauto.model.Tipo;

public class TipoAutomovelActivity extends AppCompatActivity implements AdapterTipo.OnClickListener {

    private RecyclerView rv_tipo;
    private AdapterTipo adapterTipo;

    private boolean todosTipos = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_tipo_automovel);


        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            todosTipos = (Boolean) bundle.getSerializable("Todos os Tipos");
        }

        configCliques();

        iniciaRv();
    }

    private void iniciaRv() {
        rv_tipo.setLayoutManager(new LinearLayoutManager(this));
        rv_tipo.setHasFixedSize(true);
        adapterTipo = new AdapterTipo(TipoList.getList(todosTipos), this);
        rv_tipo.setAdapter(adapterTipo);

    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciarComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Tipo de Motor da Aeronaves");

        rv_tipo = findViewById(R.id.rv_tipo);
    }

    @Override
    public void OnClick(Tipo tipo) {
        Intent intent = new Intent();
        intent.putExtra("tipoSelecionado", tipo);
        setResult(RESULT_OK, intent);
        finish();
    }

}