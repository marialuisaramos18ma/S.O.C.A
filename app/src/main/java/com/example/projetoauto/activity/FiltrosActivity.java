package com.example.projetoauto.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.SPFiltro;
import com.example.projetoauto.model.Filtro;

public class FiltrosActivity extends AppCompatActivity {

    private EditText edt_valor_min;
    private EditText edt_valor_max;
    private Button btn_filtrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        iniciaComponentes();
        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        configFiltros();
    }

    private void configFiltros() {
        Filtro filtro = SPFiltro.getFiltro(this);

        if (filtro.getValorMin() > 0) {
            edt_valor_min.setText(String.valueOf(filtro.getValorMin()));
        } else {
            edt_valor_min.setText("");
        }

        if (filtro.getValorMax() > 0) {
            edt_valor_max.setText(String.valueOf(filtro.getValorMax()));
        } else {
            edt_valor_max.setText("");
        }
    }

    private void configCliques() {
        findViewById(R.id.btn_limpar).setOnClickListener(v -> {
            SPFiltro.LimparFiltros(this);
            finish();
        });
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        btn_filtrar.setOnClickListener(v -> {
            recuperaValores();
            finish();
        });
    }

    private void recuperaValores() {
        String valorMinStr = edt_valor_min.getText().toString();
        String valorMaxStr = edt_valor_max.getText().toString();

        int valorMin = valorMinStr.isEmpty() ? 0 : Integer.parseInt(valorMinStr);
        int valorMax = valorMaxStr.isEmpty() ? 0 : Integer.parseInt(valorMaxStr);

        SPFiltro.setFiltro(this, "valorMin", String.valueOf(valorMin));
        SPFiltro.setFiltro(this, "valorMax", String.valueOf(valorMax));
    }

    private void iniciaComponentes() {
        edt_valor_min = findViewById(R.id.edt_valor_min);
        edt_valor_max = findViewById(R.id.edt_valor_max);
        btn_filtrar = findViewById(R.id.btn_filtrar);
    }
}
