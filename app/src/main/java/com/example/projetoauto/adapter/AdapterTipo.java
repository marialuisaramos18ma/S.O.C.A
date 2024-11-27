package com.example.projetoauto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoauto.R;
import com.example.projetoauto.model.Tipo;

import java.util.List;

public class AdapterTipo extends RecyclerView.Adapter<AdapterTipo.MyViewHolder> {

    private List<Tipo> tipoList;
    private OnClickListener onClickListener;

    public AdapterTipo(List<Tipo> tipoList, OnClickListener onClickListener) {
        this.tipoList = tipoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tipo_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tipo tipo = tipoList.get(position);

        holder.img_tipo.setImageResource(tipo.getCaminho());
        holder.txt_tipo.setText(tipo.getNome());

        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(tipo));
    }

    @Override
    public int getItemCount() {
        return tipoList.size();
    }

    public interface OnClickListener {
        void OnClick(Tipo tipo);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_tipo;
        TextView txt_tipo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tipo = itemView.findViewById(R.id.img_tipo);
            txt_tipo = itemView.findViewById(R.id.txt_tipo);
        }
    }
}
