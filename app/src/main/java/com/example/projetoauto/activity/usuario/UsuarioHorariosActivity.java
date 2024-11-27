package com.example.projetoauto.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.AdapterHorario;
import com.example.projetoauto.helper.HorarioList;
import com.example.projetoauto.model.Horario;

public class UsuarioHorariosActivity extends AppCompatActivity implements AdapterHorario.OnClickListener {

    private RecyclerView rv_horarios;
    private AdapterHorario adapterHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_horarios);

        iniciarComponentes();


        configCliques();

        iniciaRv();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaRv() {
        rv_horarios.setLayoutManager(new LinearLayoutManager(this));
        rv_horarios.setHasFixedSize(true);
        adapterHorario = new AdapterHorario(HorarioList.getHorarios(false), this);
        rv_horarios.setAdapter(adapterHorario);

    }

    private void iniciarComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Horarios Disponiveis");

        rv_horarios = findViewById(R.id.rv_horarios);
    }

    @Override
    public void OnClick(Horario horario) {
        Intent intent = new Intent();
        intent.putExtra("horarioSelecionado", horario);
        setResult(RESULT_OK, intent);
        finish();
    }
}