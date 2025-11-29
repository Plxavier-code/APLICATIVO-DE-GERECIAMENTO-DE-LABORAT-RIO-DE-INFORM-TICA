package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.service.ReservaService;

import java.util.List;

public class MinhasReservasActivity extends AppCompatActivity {

    private LinearLayout containerReservas;
    private ReservaService reservaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        reservaService = new ReservaService();
        containerReservas = findViewById(R.id.layoutMinhasReservas);

        carregarMinhasReservas();
    }

    private void carregarMinhasReservas() {
        containerReservas.removeAllViews();
        List<Reserva> minhas = reservaService.getMinhasReservas("aluno_teste_123");

        if (minhas.isEmpty()) {
            Toast.makeText(this, "Nenhuma reserva encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Reserva r : minhas) {
            View view = inflater.inflate(R.layout.reserva_item, containerReservas, false);

            // CORREÇÃO: Usando os IDs corretos do layout reserva_item.xml
            TextView tvLab = view.findViewById(R.id.text_lab_nome);
            TextView tvInfo = view.findViewById(R.id.text_reserva_info);
            ImageButton btnAcao = view.findViewById(R.id.btn_edit_reserva);

            // Busca e exibe o nome do laboratório
            Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(r.getLaboratorioId());
            if (lab != null) {
                tvLab.setText(lab.getNome());
            } else {
                tvLab.setText(r.getLaboratorioId()); // Fallback para o ID
            }

            // Define as informações de data e horário
            String info = "Data: " + r.getData() + " - Horário: " + r.getHorarioFormatado();
            tvInfo.setText(info);

            // A lógica continua sendo para CANCELAR, apesar do ID do botão ser de editar
            btnAcao.setOnClickListener(v -> {
                reservaService.cancelarReserva(r.getIdReserva());
                Toast.makeText(MinhasReservasActivity.this, "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                carregarMinhasReservas(); // Recarrega a lista
            });

            containerReservas.addView(view);
        }
    }
}
