package com.example.projetoauto.fragment.usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import com.example.projetoauto.R;

public class UsuarioAgendaFragment extends Fragment {

    private EditText edtCapacidadeTanque;
    private Spinner spinnerTipoCombustivel, spinnerQuantidadeMotores;
    private TextView tvVelocidadeCruzeiro, tvConsumoHora, tvResultado;
    private Button btnCalcular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora_autonomia, container, false);

        // Inicializar os componentes da interface do usuário
        edtCapacidadeTanque = view.findViewById(R.id.edt_capacidade_tanque);
        spinnerTipoCombustivel = view.findViewById(R.id.spinner_tipo_combustivel);
        spinnerQuantidadeMotores = view.findViewById(R.id.spinner_quantidade_motores);
        tvVelocidadeCruzeiro = view.findViewById(R.id.tv_velocidade_cruzeiro);
        tvConsumoHora = view.findViewById(R.id.tv_consumo_hora);
        tvResultado = view.findViewById(R.id.tv_resultado);
        btnCalcular = view.findViewById(R.id.btn_calcular);

        // Adicionar adaptadores aos spinners se necessário
        ArrayAdapter<CharSequence> adapterTipoCombustivel = ArrayAdapter.createFromResource(getContext(),
                R.array.tipo_combustivel, android.R.layout.simple_spinner_item);
        adapterTipoCombustivel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoCombustivel.setAdapter(adapterTipoCombustivel);

        ArrayAdapter<CharSequence> adapterQuantidadeMotores = ArrayAdapter.createFromResource(getContext(),
                R.array.quantidade_motores, android.R.layout.simple_spinner_item);
        adapterQuantidadeMotores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantidadeMotores.setAdapter(adapterQuantidadeMotores);

        // Atualizar labels de velocidade e consumo em tempo real
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizarLabels();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        spinnerTipoCombustivel.setOnItemSelectedListener(onItemSelectedListener);
        spinnerQuantidadeMotores.setOnItemSelectedListener(onItemSelectedListener);

        btnCalcular.setOnClickListener(v -> calcularAutonomia());

        return view;
    }

    private void atualizarLabels() {
        String tipoCombustivel = spinnerTipoCombustivel.getSelectedItem().toString();
        int quantidadeMotores = Integer.parseInt(spinnerQuantidadeMotores.getSelectedItem().toString());

        double velocidadeBase = 220; // Velocidade base para 1 motor
        double consumoBase = 6; // Consumo base para 1 motor

        // Calcular velocidade de cruzeiro
        double velocidadeCruzeiro = velocidadeBase * (1 + 0.4 * (quantidadeMotores - 1));
        if ("Querosene".equals(tipoCombustivel)) {
            velocidadeCruzeiro *= 1.1; // Aumenta 10% se for querosene
        }

        // Calcular consumo por hora
        double consumoHora = consumoBase * Math.pow(1.5, (quantidadeMotores - 1));

        tvVelocidadeCruzeiro.setText(String.format("Velocidade de Cruzeiro: %.2f KM/h", velocidadeCruzeiro));
        tvConsumoHora.setText(String.format("Consumo por Hora: %.2f Kg/h", consumoHora));
    }

    private void calcularAutonomia() {
        double capacidadeTanque = Double.parseDouble(edtCapacidadeTanque.getText().toString());
        String tipoCombustivel = spinnerTipoCombustivel.getSelectedItem().toString();
        int quantidadeMotores = Integer.parseInt(spinnerQuantidadeMotores.getSelectedItem().toString());

        double velocidadeBase = 220;
        double consumoBase = 6;

        double velocidadeCruzeiro = velocidadeBase * (1 + 0.4 * (quantidadeMotores - 1));
        if ("Querosene".equals(tipoCombustivel)) {
            velocidadeCruzeiro *= 1.1;
        }

        double consumoHora = consumoBase * Math.pow(1.5, (quantidadeMotores - 1));

        double horasAutonomia = capacidadeTanque / consumoHora;
        double distanciaAutonomia = horasAutonomia * velocidadeCruzeiro;

        String resultado = String.format("Autonomia: %.2f horas (%.2f KM)", horasAutonomia, distanciaAutonomia);
        tvResultado.setText(resultado);
    }
}



