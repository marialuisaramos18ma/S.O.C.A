package com.example.projetoauto.fragment.empresa;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class EmpresaAgendaFragment extends Fragment {

    private Spinner spinnerAeronaves, spinnerTipoCombustivel;
    private EditText edtCombustivel, edtConsumoPorHora;
    private Button btnCalcular;
    private TextView tvResultado;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora_autonomia, container, false);

        spinnerAeronaves = view.findViewById(R.id.spinner_aeronaves);
        spinnerTipoCombustivel = view.findViewById(R.id.spinner_tipo_combustivel);
        edtCombustivel = view.findViewById(R.id.edt_combustivel);
        edtConsumoPorHora = view.findViewById(R.id.edt_consumo_por_hora);
        btnCalcular = view.findViewById(R.id.btn_calcular);
        tvResultado = view.findViewById(R.id.tv_resultado);

        firestore = FirebaseFirestore.getInstance();

        loadAutomoveis();

        spinnerAeronaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Automovel selectedAutomovel = (Automovel) spinnerAeronaves.getSelectedItem();
                updateFields(selectedAutomovel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularAutonomia();
            }
        });

        return view;
    }

    private void loadAutomoveis() {
        firestore.collection("automoveis_publicos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Automovel> automoveis = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Automovel automovel = document.toObject(Automovel.class);
                            automovel.setId(document.getId());
                            automoveis.add(automovel);
                        }
                        ArrayAdapter<Automovel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, automoveis);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerAeronaves.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Erro ao carregar automóveis.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateFields(Automovel automovel) {
        // Update the fields with the selected aircraft's data
        // Example:
        edtCombustivel.setText(String.valueOf(automovel.getCombustivel())); // Assuming you have this field
        // Populate other fields based on the selected automovel
    }

    private void calcularAutonomia() {
        String combustivelStr = edtCombustivel.getText().toString().trim();
        String consumoPorHoraStr = edtConsumoPorHora.getText().toString().trim();

        if (!combustivelStr.isEmpty() && !consumoPorHoraStr.isEmpty()) {
            double combustivel = Double.parseDouble(combustivelStr);
            double consumoPorHora = Double.parseDouble(consumoPorHoraStr);
            double autonomia = combustivel / consumoPorHora;

            tvResultado.setText("Autonomia: " + autonomia + " horas");
        } else {
            Toast.makeText(getActivity(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }
}
