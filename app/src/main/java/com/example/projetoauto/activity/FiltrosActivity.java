package com.example.projetoauto.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.projetoauto.R;
import com.example.projetoauto.helper.SPFiltro;
import com.example.projetoauto.model.Filtro;
import com.example.projetoauto.model.Tipo;

import java.util.Locale;

public class FiltrosActivity extends AppCompatActivity {

    private CurrencyEditText edt_valor_min;

    private CurrencyEditText edt_valor_max;

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
            edt_valor_min.setValue(filtro.getValorMin() * 100);
        } else {
            edt_valor_min.setValue(0);
        }

        if (filtro.getValorMax() > 0) {
            edt_valor_max.setValue(filtro.getValorMax() * 100);
        } else {
            edt_valor_max.setValue(0);
        }

    }

    private void configCliques() {
        findViewById(R.id.btn_limpar).setOnClickListener(v -> {
            SPFiltro.LimparFiltros(this);
            finish();
        });
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        findViewById(R.id.btn_filtrar).setOnClickListener(v -> {
            recuperaValores();
            finish();
        });
    }

    private void recuperaValores() {
        String valorMin = String.valueOf(edt_valor_min.getRawValue() / 100);
        String valorMax = String.valueOf(edt_valor_max.getRawValue() / 100);

        SPFiltro.setFiltro(this, "valorMin", valorMin);
        SPFiltro.setFiltro(this, "valorMax", valorMax);
    }

    private void iniciaComponentes() {

        edt_valor_min = findViewById(R.id.edt_valor_min);
        edt_valor_max = findViewById(R.id.edt_valor_max);

        edt_valor_min.setLocale(new Locale("PT", "br"));
        edt_valor_max.setLocale(new Locale("PT", "br"));
    }

}