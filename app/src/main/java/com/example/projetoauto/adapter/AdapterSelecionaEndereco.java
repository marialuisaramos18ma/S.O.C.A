package com.example.projetoauto.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoauto.R;
import com.example.projetoauto.model.Endereco;


import java.util.List;

public class AdapterSelecionaEndereco extends RecyclerView.Adapter<AdapterSelecionaEndereco.MyViewHolder> {

    private final List<Endereco> enderecoList;
    private final OnClickListener onClickListener;

    private int lastSelectPosition = -1;

    public AdapterSelecionaEndereco(List<Endereco> enderecoList, OnClickListener onClickListener) {
        this.enderecoList = enderecoList;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.endereco_selecionado, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Endereco endereco = enderecoList.get(position);

        holder.text_logradouro.setText(endereco.getLogradouro());

        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getBairro())
                .append("-")
                .append(endereco.getUf());

        holder.text_endereco.setText(enderecoCompleto);

        if (lastSelectPosition == position) {
            holder.radioButton.setChecked(true);
        }

        holder.radioButton.setOnClickListener(v -> {
            lastSelectPosition = position;
            notifyDataSetChanged();
            onClickListener.OnClick(endereco);

        });


    }

    @Override
    public int getItemCount() {
        return enderecoList.size();
    }

    public interface OnClickListener {
        void OnClick(Endereco endereco);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;
        TextView text_logradouro, text_endereco;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radioButton);
            text_logradouro = itemView.findViewById(R.id.text_logradouro);
            text_endereco = itemView.findViewById(R.id.text_endereco);
        }
    }
}
