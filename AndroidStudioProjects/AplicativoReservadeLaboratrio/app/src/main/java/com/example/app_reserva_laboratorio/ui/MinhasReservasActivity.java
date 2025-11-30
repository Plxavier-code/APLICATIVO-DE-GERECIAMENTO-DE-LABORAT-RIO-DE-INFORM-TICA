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
import com.example.app_reserva_laboratorio.data.Usuario;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.example.app_reserva_laboratorio.session.SessionManager;
import com.example.app_reserva_laboratorio.util.NotificationHelper; // IMPORT ADICIONADO

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
        
        Usuario usuarioLogado = SessionManager.getInstance().getUsuarioLogado();
        if (usuarioLogado == null) {
            Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        List<Reserva> minhasReservas = reservaService.getMinhasReservas(usuarioLogado.getId());

        if (minhasReservas.isEmpty()) {
            Toast.makeText(this, "Nenhuma reserva encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Reserva reserva : minhasReservas) {
            View view = inflater.inflate(R.layout.reserva_item, containerReservas, false);

            TextView tvLab = view.findViewById(R.id.text_lab_nome);
            TextView tvInfo = view.findViewById(R.id.text_reserva_info);
            ImageButton btnAcao = view.findViewById(R.id.btn_edit_reserva);

            Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(reserva.getLaboratorioId());
            tvLab.setText(lab != null ? lab.getNome() : reserva.getLaboratorioId());

            String info = "Data: " + reserva.getData() + " - Horário: " + reserva.getHorarioFormatado();
            tvInfo.setText(info);

            btnAcao.setOnClickListener(v -> {
                // 1. Guarda a referência da reserva antes de removê-la
                Reserva reservaCancelada = reserva;

                // 2. Remove a reserva do repositório
                reservaService.cancelarReserva(reservaCancelada.getIdReserva());

                // 3. Notifica o usuário
                Toast.makeText(MinhasReservasActivity.this, "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                NotificationHelper.notificarSobreCancelamento(getApplicationContext(), reservaCancelada);

                // 4. Recarrega a lista para atualizar a UI
                carregarMinhasReservas();
            });

            containerReservas.addView(view);
        }
    }
}
