package com.example.app_reserva_laboratorio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import java.util.List;

public class EstacaoAdapter extends RecyclerView.Adapter<EstacaoAdapter.EstacaoViewHolder> {

    private final List<Laboratorio> estacoes;
    private int selectedPosition = -1; // Posição do item selecionado
    private final Context context;

    public EstacaoAdapter(Context context, List<Laboratorio> estacoes) {
        this.context = context;
        this.estacoes = estacoes;
    }

    @NonNull
    @Override
    public EstacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.estacao_item, parent, false);
        return new EstacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstacaoViewHolder holder, int position) {
        Laboratorio estacao = estacoes.get(position);
        
        // Extrai apenas o número da estação do nome
        String numeroEstacao = estacao.getNome().replaceAll("[^0-9]", "");
        holder.textViewNumeroEstacao.setText(numeroEstacao);

        // Altera a aparência do item se ele estiver selecionado
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selected_item_color));
            holder.textViewNumeroEstacao.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.textViewNumeroEstacao.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        // Define o listener de clique
        holder.itemView.setOnClickListener(v -> {
            // Desmarca a seleção anterior
            notifyItemChanged(selectedPosition);
            // Marca a nova seleção
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return estacoes.size();
    }

    /**
     * Retorna a estação que foi selecionada pelo usuário.
     * @return O objeto Laboratorio da estação selecionada, ou null se nenhuma for selecionada.
     */
    public Laboratorio getEstacaoSelecionada() {
        if (selectedPosition != -1) {
            return estacoes.get(selectedPosition);
        }
        return null;
    }

    // ViewHolder que representa cada item da lista
    static class EstacaoViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewNumeroEstacao;

        public EstacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_estacao);
            textViewNumeroEstacao = itemView.findViewById(R.id.text_view_numero_estacao);
        }
    }
}
