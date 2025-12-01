package com.example.app_reserva_laboratorio.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import java.util.List;

public class AdminLabsAdapter extends RecyclerView.Adapter<AdminLabsAdapter.LabAdminViewHolder> {

    private List<Laboratorio> listaLabs;
    private OnLabAdminClickListener clickListener;
    private Context context;

    public AdminLabsAdapter(List<Laboratorio> listaLabs, OnLabAdminClickListener clickListener, Context context) {
        this.listaLabs = listaLabs;
        this.clickListener = clickListener;
        this.context = context;
    }

    public interface OnLabAdminClickListener {
        void onManutencaoClick(Laboratorio lab);
        void onBloquearClick(Laboratorio lab);
    }

    @NonNull
    @Override
    public LabAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_admin_item, parent, false);
        return new LabAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabAdminViewHolder holder, int position) {
        Laboratorio lab = listaLabs.get(position);

        holder.textLabNome.setText(lab.getNome());
        holder.textLabLocal.setText(lab.getLocalizacao());

        if (lab.isDisponivel()) {
            holder.btnBloquear.setText("Bloquear");
            holder.btnBloquear.setBackgroundColor(Color.parseColor("#E53935")); // Vermelho
        } else {
            holder.btnBloquear.setText("Desbloquear");
            holder.btnBloquear.setBackgroundColor(Color.GRAY);
        }

        if (lab.isEmManutencao()) {
            holder.btnManutencao.setText("Concluir Man.");
            holder.btnManutencao.setBackgroundColor(Color.parseColor("#546E7A")); // Cinza azulado
        } else {
            holder.btnManutencao.setText("Manutenção");
            holder.btnManutencao.setBackgroundColor(Color.parseColor("#3471FA")); // Azul
        }

        holder.btnManutencao.setOnClickListener(v -> clickListener.onManutencaoClick(lab));
        holder.btnBloquear.setOnClickListener(v -> clickListener.onBloquearClick(lab));
    }

    @Override
    public int getItemCount() {
        return listaLabs.size();
    }

    public static class LabAdminViewHolder extends RecyclerView.ViewHolder {
        TextView textLabNome;
        TextView textLabLocal;
        Button btnManutencao;
        Button btnBloquear;

        public LabAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textLabNome = itemView.findViewById(R.id.text_admin_lab_nome);
            textLabLocal = itemView.findViewById(R.id.text_admin_lab_local);
            btnManutencao = itemView.findViewById(R.id.btn_admin_manutencao);
            btnBloquear = itemView.findViewById(R.id.btn_admin_bloquear);
        }
    }
}
