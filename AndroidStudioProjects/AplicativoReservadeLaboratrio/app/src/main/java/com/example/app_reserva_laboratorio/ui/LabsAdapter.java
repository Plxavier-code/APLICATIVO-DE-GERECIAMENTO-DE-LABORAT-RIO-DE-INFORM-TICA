package com.example.app_reserva_laboratorio.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import java.util.List;

public class LabsAdapter extends RecyclerView.Adapter<LabsAdapter.LabViewHolder> {

    private List<Laboratorio> laboratorios;
    private Context context;

    public LabsAdapter(Context context, List<Laboratorio> laboratorios) {
        this.context = context;
        this.laboratorios = laboratorios;
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CORRIGIDO: Apontando para o layout correto do item da lista
        View view = LayoutInflater.from(context).inflate(R.layout.lab_item_user, parent, false);
        return new LabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabViewHolder holder, int position) {
        Laboratorio lab = laboratorios.get(position);
        holder.textNome.setText(lab.getNome());
        holder.textLocal.setText(lab.getLocalizacao());
        holder.textCapacidade.setText("Capacidade: " + lab.getCapacidade() + " computadores");

        int imageResId = context.getResources().getIdentifier(lab.getUrlImagem(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imageLab.setImageResource(imageResId);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RealizarReservaActivity.class);
            intent.putExtra("LAB_ID", lab.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return laboratorios.size();
    }

    public static class LabViewHolder extends RecyclerView.ViewHolder {
        ImageView imageLab;
        TextView textNome;
        TextView textCapacidade;
        TextView textLocal;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLab = itemView.findViewById(R.id.image_lab);
            textNome = itemView.findViewById(R.id.text_lab_nome);
            textCapacidade = itemView.findViewById(R.id.text_lab_capacidade);
            textLocal = itemView.findViewById(R.id.text_lab_local);
        }
    }
}
