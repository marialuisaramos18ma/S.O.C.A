package com.example.projetoauto.activity.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Agendamento;
import com.example.projetoauto.model.Automovel;
import com.example.projetoauto.model.Horario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UsuarioCalendarioActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private CalendarView CalendarView_calendario;

    private Button btn_horarios;
    private TextView txt_horario;

    private EditText edt_descricao;

    private int dia_atual;
    private int mes_atual;
    private int ano_atual;

    private final int REQUEST_HORARIO = 100;

    private String horarioSelecionado = "";

    private Agendamento agendamento;

    private Automovel automovel;

    private boolean novoAgendamento = true;

    private Long dataLong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_calendario);

        iniciaComponentes();

        obterDataAtual();

        configCalendario();

        configCliques();


    }


    // Configuração de Calendario
    private void configCalendario() {

        Calendar dataMinima = configurarDataMinima();
        Calendar dataMaxima = configurarDataMaxima();

        CalendarView_calendario.setMinDate(dataMinima.getTimeInMillis());
        CalendarView_calendario.setMaxDate(dataMaxima.getTimeInMillis());


    }

    private Calendar configurarDataMinima() {

        Calendar dataMinima = Calendar.getInstance();
        int diaInicioCalendario = 1;

        dataMinima.set(ano_atual, mes_atual - 1, diaInicioCalendario);

        return dataMinima;
    }

    private Calendar configurarDataMaxima() {

        Calendar dataMaxima = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        int diaFinalCalendario = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dia_atual == diaFinalCalendario) {

            dataMaxima.set(ano_atual, mes_atual, 15);

        } else {

            dataMaxima.set(ano_atual, mes_atual - 1, diaFinalCalendario);
        }
        return dataMaxima;
    }

    public void obterDataAtual() {

        dataLong = CalendarView_calendario.getDate();

        Locale locale = new Locale("pt", "BR");

        SimpleDateFormat dia = new SimpleDateFormat("dd", locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM", locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy", locale);

        dia_atual = Integer.parseInt(dia.format(dataLong));

        mes_atual = Integer.parseInt(mes.format(dataLong));

        ano_atual = Integer.parseInt(ano.format(dataLong));


    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int anoSelecionado, int mesSelecionado, int diaSelecionado) {

        int mes = mesSelecionado + 1;

        dataSelecionada(diaSelecionado, mes, anoSelecionado);

    }

    private void dataSelecionada(int diaSelecionado, int mesSelecionado, int anoSelecionado) {

        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        Calendar data = Calendar.getInstance();


        try {
            data.setTime(simpleDateFormat.parse(diaSelecionado + "/" + mesSelecionado + "/" + anoSelecionado));

            boolean disponivelAgendamento;

            if (mesSelecionado != mes_atual) {

                disponivelAgendamento = true;

            } else {

                disponivelAgendamento = agendaDisponivel(data, diaSelecionado);

            }


            if (disponivelAgendamento) {

                if (com.example.projetoauto.util.Util.statusInternet_MoWi(this)) {

                    String dia = String.valueOf(diaSelecionado);
                    String mes = String.valueOf(mesSelecionado);
                    String ano = String.valueOf(anoSelecionado);

                    ArrayList<String> dataList = new ArrayList<String>();

                    dataList.add(dia);// 0
                    dataList.add(mes);// 1
                    dataList.add(ano);// 2

                    Toast.makeText(this, "Agendamento Disponivel", Toast.LENGTH_SHORT).show();
                    configbtn(true);


                } else {
                    Toast.makeText(this, "Erro - Sem conexão com a internet", Toast.LENGTH_LONG).show();
                }


            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private boolean agendaDisponivel(Calendar data, int diaSelecionado) {

        Calendar calendar = Calendar.getInstance();

        int diaFinalCalendario = calendar.getActualMaximum(mes_atual - 1);

        if (diaFinalCalendario == dia_atual) {

            Toast.makeText(this, "Agendamento disponivel apartir do dia 1 ", Toast.LENGTH_LONG).show();
            configbtn(false);
            return false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

            Toast.makeText(this, "Infelizmente não trabalhamos no domingo.", Toast.LENGTH_LONG).show();
            configbtn(false);
            return false;
        } else if (diaSelecionado <= dia_atual) {

            Toast.makeText(this, "Agendamento disponivel apartir do dia " + (dia_atual + 1), Toast.LENGTH_LONG).show();
            configbtn(false);
            return false;

        } else {
            return true;
        }

    }

    // Config Horario

    public void selecionaHorario(View view) {
        Intent intent = new Intent(this, UsuarioHorariosActivity.class);
        startActivityForResult(intent, REQUEST_HORARIO);
    }

    private void configbtn(boolean disponivel) {
        if (disponivel == true) {
            txt_horario.setVisibility(View.VISIBLE);
            btn_horarios.setVisibility(View.VISIBLE);
        } else {
            txt_horario.setVisibility(View.GONE);
            btn_horarios.setVisibility(View.GONE);
        }
    }

    private void erroSalvarHorario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Selecione um horario para atendimento");
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Condiguração da pagina

    public void validaDados(View view) {

        String descricao = edt_descricao.getText().toString().trim();

        if (!horarioSelecionado.isEmpty()) {
            if (!descricao.isEmpty()) {

                if (agendamento == null) agendamento = new Agendamento();

                agendamento.setData(dataLong);
                agendamento.setHorario(horarioSelecionado);
                agendamento.setDescricao(descricao);
                agendamento.setIdAutomovel(FirebaseHelper.getIdFirebase());

                if (novoAgendamento) {
                    agendamento.Salvar(this);
                }


            } else {
                edt_descricao.requestFocus();
                edt_descricao.setError("Informe uma Descrição");
            }
        } else {
            btn_horarios.requestFocus();
            ocultarTeclado();
            erroSalvarHorario();
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        CalendarView_calendario.setOnDateChangeListener(this);

    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Agendamento");

        CalendarView_calendario = findViewById(R.id.CalendarView_calendario);

        btn_horarios = findViewById(R.id.btn_horarios);
        btn_horarios.setVisibility(View.GONE);

        txt_horario = findViewById(R.id.txt_horario);
        txt_horario.setVisibility(View.GONE);

        edt_descricao = findViewById(R.id.edt_descricao);
    }


    // Config horario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_HORARIO) {

                Horario horario = (Horario) data.getSerializableExtra("horarioSelecionado");
                horarioSelecionado = horario.getHora();
                btn_horarios.setText(horarioSelecionado);
            } else {

            }
        }
    }

    private void ocultarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btn_horarios.getWindowToken(), 0);
    }


}