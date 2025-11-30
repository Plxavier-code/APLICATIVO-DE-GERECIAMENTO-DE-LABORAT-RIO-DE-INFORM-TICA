package com.example.a2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2.R;
import com.example.a2.data.Laboratorio;
import com.example.a2.data.Reserva;
import com.example.a2.data.ReservaRepository;
import com.example.a2.data.Usuario;
import com.example.a2.service.ReservaService;
import com.example.a2.service.SessionManager;
import com.example.a2.util.NotificationHelper; // Importa a classe de notificação

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarMinhasReservas();
    }

    private void carregarMinhasReservas() {
        containerReservas.removeAllViews();
        
        Usuario usuarioLogado = SessionManager.getInstance().getUsuarioLogado();
        if (usuarioLogado == null) {
            Toast.makeText(this, "Sessão inválida.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        List<Reserva> minhasReservas = reservaService.buscarMinhasReservas(usuarioLogado.getId());

        if (minhasReservas.isEmpty()) {
            Toast.makeText(this, "Nenhuma reserva encontrada.", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Reserva reserva : minhasReservas) {
            View view = inflater.inflate(R.layout.list_item_minha_reserva, containerReservas, false);

            TextView tvLab = view.findViewById(R.id.tvCardLabNome);
            TextView tvData = view.findViewById(R.id.tvCardData);
            TextView tvDescricao = view.findViewById(R.id.tvCardDescricao);
            ImageButton btnCancelar = view.findViewById(R.id.btnCancelarReserva);

            Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(reserva.getLaboratorioId());
            tvLab.setText(lab != null ? lab.getNome() : reserva.getLaboratorioId());
            tvData.setText("Data: " + reserva.getData() + " - Horário: " + reserva.getHorarioFormatado());
            tvDescricao.setText("Motivo: " + reserva.getDescricao());

            btnCancelar.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Cancelar Reserva")
                        .setMessage("Tem certeza que deseja cancelar esta reserva?")
                        .setPositiveButton("Sim, Cancelar", (dialog, which) -> {
                            // *** Dispara a notificação de cancelamento ***
                            NotificationHelper.notificarSobreCancelamento(getApplicationContext(), reserva);
                            reservaService.cancelarReserva(reserva.getIdReserva());
                            Toast.makeText(MinhasReservasActivity.this, "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                            carregarMinhasReservas();
                        })
                        .setNegativeButton("Não", null)
                        .show();
            });

            containerReservas.addView(view);
        }
    }
}
