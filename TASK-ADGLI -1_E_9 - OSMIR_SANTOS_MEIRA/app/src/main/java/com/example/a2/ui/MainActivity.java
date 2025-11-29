package com.example.a2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.example.a2.data.Reserva;
import com.example.a2.service.ReservaService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Service
    private ReservaService reservaService;

    // Componentes da UI
    private Toolbar toolbar;
    private ProgressBar progressBarMain;
    private LinearLayout layoutReservas;
    private TextView tvWelcome, tvNoReservas;
    private FloatingActionButton fabNovaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o service
        reservaService = new ReservaService();

        // Vincula os componentes do layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBarMain = findViewById(R.id.progressBarMain);
        layoutReservas = findViewById(R.id.layoutReservas);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvNoReservas = findViewById(R.id.tvNoReservas);
        fabNovaReserva = findViewById(R.id.fabNovaReserva);

        // Mensagem de boas-vindas genérica
        tvWelcome.setText("Minhas Reservas");

        // Configura o clique do botão FAB para iniciar o fluxo de agendamento
        fabNovaReserva.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpcoesReservaActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservas(); // Carrega as reservas sempre que a tela se torna visível
    }

    private void loadReservas() {
        showLoading(true);
        layoutReservas.removeAllViews();
        tvNoReservas.setVisibility(View.GONE);

        // Busca as reservas do serviço local
        List<Reserva> minhasReservas = reservaService.buscarMinhasReservas("user_padrao");

        if (minhasReservas.isEmpty()) {
            tvNoReservas.setVisibility(View.VISIBLE);
        } else {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Reserva reserva : minhasReservas) {
                View reservaCard = inflater.inflate(R.layout.list_item_minha_reserva, layoutReservas, false);

                TextView tvNome = reservaCard.findViewById(R.id.tvCardLabNome);
                TextView tvData = reservaCard.findViewById(R.id.tvCardData);
                TextView tvDescricao = reservaCard.findViewById(R.id.tvCardDescricao);
                ImageButton btnCancelar = reservaCard.findViewById(R.id.btnCancelarReserva);

                // Preenche os dados no card
                tvNome.setText(String.format("%s (%s)", reserva.getLaboratorioId(), reserva.getHorarioFormatado()));
                tvData.setText("Dia: " + reserva.getData());
                tvDescricao.setText("Motivo: " + reserva.getDescricao());

                btnCancelar.setOnClickListener(v -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Cancelar Reserva")
                            .setMessage("Tem certeza que deseja cancelar esta reserva?")
                            .setPositiveButton("Sim, Cancelar", (dialog, which) -> {
                                Toast.makeText(MainActivity.this, "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                                loadReservas(); // Apenas recarrega a lista
                            })
                            .setNegativeButton("Não", null)
                            .show();
                });

                layoutReservas.addView(reservaCard);
            }
        }
        showLoading(false);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBarMain.setVisibility(View.VISIBLE);
        } else {
            progressBarMain.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // O menu de logout foi removido, então não há mais ações aqui.
        // Pode ser usado para outras funcionalidades no futuro.
        return super.onOptionsItemSelected(item);
    }
}
