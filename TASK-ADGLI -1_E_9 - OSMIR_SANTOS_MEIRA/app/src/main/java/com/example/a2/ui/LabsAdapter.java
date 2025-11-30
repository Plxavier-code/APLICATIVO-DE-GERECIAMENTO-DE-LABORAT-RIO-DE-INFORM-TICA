package com.example.a2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2.R;
import com.example.a2.data.Laboratorio;

import java.util.List;

public class LabsAdapter extends RecyclerView.Adapter<LabsAdapter.LabViewHolder> {

    private final List<Laboratorio> laboratorios;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Laboratorio laboratorio);
    }

    public LabsAdapter(List<Laboratorio> laboratorios, OnItemClickListener listener) {
        this.laboratorios = laboratorios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_laboratorio, parent, false);
        return new LabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabViewHolder holder, int position) {
        Laboratorio lab = laboratorios.get(position);
        holder.bind(lab, listener);
    }

    @Override
    public int getItemCount() {
        return laboratorios.size();
    }

    static class LabViewHolder extends RecyclerView.ViewHolder {
        private final TextView textNome;
        private final TextView textCapacidade;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.tvLabNome);
            textCapacidade = itemView.findViewById(R.id.tvLabCapacidade);
        }

        public void bind(final Laboratorio laboratorio, final OnItemClickListener listener) {
            textNome.setText(laboratorio.getNome());
            textCapacidade.setText("Capacidade: " + laboratorio.getCapacidade());
            itemView.setOnClickListener(v -> listener.onItemClick(laboratorio));
        }
    }
}
