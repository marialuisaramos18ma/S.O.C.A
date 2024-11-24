package com.example.projetoauto.fragment.usuario;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projetoauto.R;
import com.example.projetoauto.adapter.AdapterListaAgendamento;
import com.example.projetoauto.adapter.AdapterListaAutomovel;
import com.example.projetoauto.helper.FirebaseHelper;
import com.example.projetoauto.model.Agendamento;
import com.example.projetoauto.model.Automovel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioAgendaFragment extends Fragment implements AdapterListaAgendamento.OnclickListener {

    private AdapterListaAgendamento adapterListaAgendamento;

    private final List<Agendamento> agendamentoList = new ArrayList<>();

    private SwipeableRecyclerView rv_agendamentos;

    private ProgressBar progressBar;

    private TextView text_info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario_agenda, container, false);

        iniciarComponentes(view);

        configRv();



        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        recuperaAgendamento();
    }

    private void configRv() {
        rv_agendamentos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_agendamentos.setHasFixedSize(true);
        adapterListaAgendamento = new AdapterListaAgendamento(agendamentoList, this);
        rv_agendamentos.setAdapter(adapterListaAgendamento);

        rv_agendamentos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                //Delete
                showDialogDelete(agendamentoList.get(position));
            }

            @Override
            public void onSwipedRight(int position) {
                //Edit
            }
        });
    }
    private void showDialogDelete(Agendamento agendamento) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Deseja remover o Agendamento ?");
        alertDialog.setNegativeButton("Não", ((dialog, which) -> {
            dialog.dismiss();
            adapterListaAgendamento.notifyDataSetChanged();
        })).setPositiveButton("Sim", ((dialog, which) -> {
            agendamentoList.remove(agendamento);
            agendamento.remover();

            adapterListaAgendamento.notifyDataSetChanged();
        }));

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void recuperaAgendamento() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference agendamentoRef = FirebaseHelper.getDatabaseReference()
                    .child("agendamentos")
                    .child(FirebaseHelper.getIdFirebase());

            agendamentoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        agendamentoList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Agendamento agendamento = ds.getValue(Agendamento.class);
                            agendamentoList.add(agendamento);

                            if (agendamentoList.isEmpty()) {
                                text_info.setText("Nenhum agendamento encontrado.");
                            } else {
                                text_info.setText("");
                            }

                        }
                        text_info.setText("");
                        Collections.reverse(agendamentoList);
                        adapterListaAgendamento.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            text_info.setText("");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnClick(Agendamento agendamento) {

    }

    private void iniciarComponentes(View view) {
        rv_agendamentos = view.findViewById(R.id.rv_agendamentos);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);

    }
}