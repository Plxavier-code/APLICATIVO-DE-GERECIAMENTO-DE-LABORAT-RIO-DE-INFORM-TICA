package com.example.app_reserva_laboratorio.ui;

import android.app.Activity;
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

public class EstacoesAdapter extends RecyclerView.Adapter<EstacoesAdapter.EstacaoViewHolder> {

    private List<Laboratorio> estacoes;
    private Context context;

    public EstacoesAdapter(Context context, List<Laboratorio> estacoes) {
        this.context = context;
        this.estacoes = estacoes;
    }

    @NonNull
    @Override
    public EstacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.estacao_item_user, parent, false);
        return new EstacaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstacaoViewHolder holder, int position) {
        Laboratorio estacao = estacoes.get(position);
        holder.textNome.setText(estacao.getNome());
        holder.textLocal.setText(estacao.getLocalizacao());

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String fluxo = ((Activity) context).getIntent().getStringExtra("fluxo");

            if ("manutencao_estacao".equals(fluxo)) {
                intent = new Intent(context, ConfirmacaoManutencaoActivity.class);
                intent.putExtra(ConfirmacaoManutencaoActivity.EXTRA_ITEM_ID, estacao.getId());
            } else {
                intent = new Intent(context, RealizarEstacaoActivity.class);
                intent.putExtra("ESTACAO_ID", estacao.getId());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return estacoes.size();
    }

    public static class EstacaoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageEstacao;
        TextView textNome;
        TextView textLocal;

        public EstacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageEstacao = itemView.findViewById(R.id.image_estacao);
            textNome = itemView.findViewById(R.id.text_estacao_nome);
            textLocal = itemView.findViewById(R.id.text_estacao_local);
        }
    }
}
