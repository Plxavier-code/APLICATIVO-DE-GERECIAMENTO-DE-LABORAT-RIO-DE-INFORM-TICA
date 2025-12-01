package com.example.app_reserva_laboratorio.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Software;

import java.util.List;

public class SoftwareAdapter extends RecyclerView.Adapter<SoftwareAdapter.SoftwareViewHolder> {

    private List<Software> softwareList;

    public SoftwareAdapter(List<Software> softwareList) {
        this.softwareList = softwareList;
    }

    @NonNull
    @Override
    public SoftwareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.software_item, parent, false);
        return new SoftwareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoftwareViewHolder holder, int position) {
        Software software = softwareList.get(position);
        holder.textViewNome.setText(software.getNome());
        holder.textViewVersao.setText(software.getVersao());
    }

    @Override
    public int getItemCount() {
        return softwareList.size();
    }

    static class SoftwareViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome;
        TextView textViewVersao;

        public SoftwareViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.text_view_nome);
            textViewVersao = itemView.findViewById(R.id.text_view_versao);
        }
    }
}
