package com.example.projetoauto.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.projetoauto.R;
import com.example.projetoauto.model.Automovel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraAutonomiaFragment extends Fragment {

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

        // Verificar se os componentes foram inicializados corretamente
        if (spinnerTipoCombustivel != null && spinnerQuantidadeMotores != null) {
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
        } else {
            Log.e("CalculadoraAutonomia", "Erro ao inicializar os Spinners.");
        }

        return view;
    }

    private void atualizarLabels() {
        String tipoCombustivel = spinnerTipoCombustivel.getSelectedItem().toString();
        int quantidadeMotores = Integer.parseInt(spinnerQuantidadeMotores.getSelectedItem().toString());

        double velocidadeBase = 220; // Velocidade base para 1 motor
        double consumoBase = 5; // Consumo base para 1 motor

        // Calcular velocidade de cruzeiro
        double velocidadeCruzeiro = velocidadeBase * (1 + 0.4 * (quantidadeMotores - 1));
        if ("Querosene".equals(tipoCombustivel)) {
            velocidadeCruzeiro *= 1.1; // Aumenta 10% se for querosene
        }

        // Calcular consumo por hora
        double consumoHora = consumoBase * Math.pow(1.3, (quantidadeMotores - 1));

        tvVelocidadeCruzeiro.setText(String.format("Velocidade de Cruzeiro: %.2f KM/h", velocidadeCruzeiro));
        tvConsumoHora.setText(String.format("Consumo por Hora: %.2f Kg/h", consumoHora));
    }

    private void calcularAutonomia() {
        double capacidadeTanque = Double.parseDouble(edtCapacidadeTanque.getText().toString());
        String tipoCombustivel = spinnerTipoCombustivel.getSelectedItem().toString();
        int quantidadeMotores = Integer.parseInt(spinnerQuantidadeMotores.getSelectedItem().toString());

        double velocidadeBase = 220;
        double consumoBase = 5;

        double velocidadeCruzeiro = velocidadeBase * (1 + 0.4 * (quantidadeMotores - 1));
        if ("Querosene".equals(tipoCombustivel)) {
            velocidadeCruzeiro *= 1.1;
        }

        double consumoHora = consumoBase * Math.pow(1.3, (quantidadeMotores - 1));

        double horasAutonomia = capacidadeTanque / consumoHora;
        double distanciaAutonomia = horasAutonomia * velocidadeCruzeiro;

        String resultado = String.format("Autonomia: %.2f horas (%.2f KM)", horasAutonomia, distanciaAutonomia);
        tvResultado.setText(resultado);
    }
}

