package com.example.a2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2.R;
import com.example.a2.data.Reserva;
import com.example.a2.service.ReservaService;

import java.util.List;

public class MinhasReservasActivity extends AppCompatActivity {

    private LinearLayout containerReservas;
    private ReservaService reservaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        reservaService = new ReservaService();
        // Garanta que o ID no seu activity_minhas_reservas.xml seja 'layoutMinhasReservas' ou ajuste aqui.
        containerReservas = findViewById(R.id.layoutMinhasReservas);

        carregarMinhasReservas();
    }

    private void carregarMinhasReservas() {
        containerReservas.removeAllViews();
        List<Reserva> minhas = reservaService.buscarMinhasReservas("user_padrao");

        if (minhas.isEmpty()) {
            Toast.makeText(this, "Nenhuma reserva encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Reserva r : minhas) {
            View view = inflater.inflate(R.layout.list_item_minha_reserva, containerReservas, false);

            TextView tvLab = view.findViewById(R.id.tvCardLabNome);
            TextView tvDescricao = view.findViewById(R.id.tvCardDescricao);
            Button btnCancelar = view.findViewById(R.id.btnCancelarReserva);

            tvLab.setText(r.getLaboratorioId()); // Idealmente buscaria o nome do laboratório
            String descricao = "Data: " + r.getData() + " - Horário: " + r.getHorarioFormatado();
            tvDescricao.setText(descricao);

            btnCancelar.setOnClickListener(v -> {
                // Supondo que ReservaService tenha um método para cancelar
                // e que Reserva tenha um getId() ou método similar.
                // reservaService.cancelarReserva(r.getId());

                Toast.makeText(MinhasReservasActivity.this, "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                carregarMinhasReservas(); // Recarrega a lista para remover o item cancelado
            });

            containerReservas.addView(view);
        }
    }
}
