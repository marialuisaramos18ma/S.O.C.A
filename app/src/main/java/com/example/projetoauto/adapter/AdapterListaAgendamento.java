package com.example.projetoauto.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoauto.R;
import com.example.projetoauto.helper.Mascara;
import com.example.projetoauto.model.Agendamento;
import com.example.projetoauto.model.Automovel;

import java.util.List;

public class AdapterListaAgendamento extends RecyclerView.Adapter<AdapterListaAgendamento.MyViewHolder> {

    private List<Agendamento> agendamentoList;
    private OnclickListener onclickListener;

    public AdapterListaAgendamento(List<Agendamento> agendamentoList, OnclickListener onclickListener) {
        this.agendamentoList = agendamentoList;
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agendamento_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Agendamento agendamento = agendamentoList.get(position);

        holder.text_data.setText(Mascara.getData(agendamento.getData(), 3));

        holder.text_hora.setText(agendamento.getHorario());

        holder.itemView.setOnClickListener(v -> onclickListener.OnClick(agendamento));


    }

    @Override
    public int getItemCount() {
        return agendamentoList.size();
    }

    public interface OnclickListener {
        void OnClick(Agendamento agendamento);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_data, text_hora;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_data = itemView.findViewById(R.id.text_data);
            text_hora = itemView.findViewById(R.id.text_hora);
        }
    }
}
