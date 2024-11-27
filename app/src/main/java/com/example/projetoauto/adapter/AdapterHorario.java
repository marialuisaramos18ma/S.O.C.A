package com.example.projetoauto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoauto.R;
import com.example.projetoauto.model.Horario;

import java.util.List;


public class AdapterHorario extends RecyclerView.Adapter<AdapterHorario.MyViewHolder> {

    private List<Horario> horariosList;
    private OnClickListener onClickListener;


    public AdapterHorario(List<Horario> horariosList, OnClickListener onClickListener) {
        this.horariosList = horariosList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horario_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Horario horario = horariosList.get(position);

        holder.txt_Item.setText(horario.getHora());

        holder.cardView_item.setOnClickListener(v -> onClickListener.OnClick(horario));
    }

    @Override
    public int getItemCount() {
        return horariosList.size();
    }

    public interface OnClickListener {
        void OnClick(Horario horario);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView_item;
        TextView txt_Item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Item = itemView.findViewById(R.id.txt_Item);
            cardView_item = itemView.findViewById(R.id.cardView_item);
        }
    }
}
