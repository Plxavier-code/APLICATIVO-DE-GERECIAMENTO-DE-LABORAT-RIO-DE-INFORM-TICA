package com.example.app_reserva_laboratorio.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;

import java.util.List;

/**
 * Adapter para o RecyclerView de Minhas Reservas.
 * Pega a lista de dados (List<Reserva>) e transforma em
 * views (reserva_item.xml).
 *
 */
public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {

    private List<Reserva> listaReservas;
    private OnReservaClickListener clickListener;
    private ReservaRepository repository;

    /**
     * Construtor do Adapter.
     * @param listaReservas A lista de dados vinda do ReservaService.
     * @param clickListener A Activity que vai "ouvir" os cliques.
     */
    public ReservasAdapter(List<Reserva> listaReservas, OnReservaClickListener clickListener, ReservaRepository repository) {
        this.listaReservas = listaReservas;
        this.clickListener = clickListener;
        this.repository = repository;
    }

    /**
     * Interface para "delegar" o clique do botão de editar
     * de volta para a Activity.
     */
    public interface OnReservaClickListener {
        void onEditClick(Reserva reserva);
    }

    /**
     * Chamado pelo RecyclerView quando ele precisa de um novo "molde" (ViewHolder).
     * Ele infla (cria) o layout do XML (reserva_item.xml).
     */
    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserva_item, parent, false);
        return new ReservaViewHolder(view);
    }

    /**
     * Chamado pelo RecyclerView para popular os dados em um item (card).
     * Pega os dados da Posição X e coloca nos TextViews.
     */
    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        // Pega a reserva específica desta posição
        Reserva reserva = listaReservas.get(position);

        // Traduzir o ID no nome
        String nomeLaboratorio = reserva.getLaboratorioId();
        Laboratorio lab = repository.getLaboratorioById(reserva.getLaboratorioId());

        if (lab != null) {
            nomeLaboratorio = lab.getNome();
        }

        holder.textLabNome.setText(nomeLaboratorio);
        holder.textReservaInfo.setText(reserva.getData() + " -> " + reserva.getHorarioFormatado());
        holder.textReservaDescricao.setText(reserva.getDescricao());

        // Define o clique para o botão de editar
        holder.btnEditReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chama o método da interface (que será implementado na Activity)
                clickListener.onEditClick(reserva);
            }
        });
    }

    /**
     * Informa ao RecyclerView quantos itens existem na lista.
     */
    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    /**
     * ViewHolder: É uma classe interna que "segura" as referências
     * para os componentes da UI (TextViews, etc.) do `reserva_item.xml`.
     */
    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView textLabNome;
        TextView textReservaInfo;
        TextView textReservaDescricao;
        ImageButton btnEditReserva;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra os componentes pelo ID
            textLabNome = itemView.findViewById(R.id.text_lab_nome);
            textReservaInfo = itemView.findViewById(R.id.text_reserva_info);
            textReservaDescricao = itemView.findViewById(R.id.text_reserva_descricao);
            btnEditReserva = itemView.findViewById(R.id.btn_edit_reserva);
        }
    }
}
