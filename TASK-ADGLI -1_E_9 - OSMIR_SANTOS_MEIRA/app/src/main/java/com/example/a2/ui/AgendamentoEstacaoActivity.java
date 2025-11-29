package com.example.a2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.example.a2.data.Reserva;
import com.example.a2.data.ReservaRepository;
import com.example.a2.service.ReservaService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgendamentoEstacaoActivity extends AppCompatActivity {

    private ReservaService reservaService;

    private TextView tvNomeEstacaoAgendamento;
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private EditText etDescricao;
    private Button btnConfirmarReserva;
    private ProgressBar progressBarAgendamento;

    private String estacaoId; // Ex: est_1_lab_h401
    private String estacaoNome;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_estacao);

        reservaService = new ReservaService();

        estacaoId = getIntent().getStringExtra("ESTACAO_ID");
        estacaoNome = getIntent().getStringExtra("ESTACAO_NOME");

        if (estacaoId == null) {
            Toast.makeText(this, "Estação inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbarAgendamento);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNomeEstacaoAgendamento = findViewById(R.id.tvNomeEstacaoAgendamento);
        calendarView = findViewById(R.id.calendarView);
        spinnerHorario = findViewById(R.id.spinnerHorario);
        etDescricao = findViewById(R.id.etDescricao);
        btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);
        progressBarAgendamento = findViewById(R.id.progressBarAgendamento);

        tvNomeEstacaoAgendamento.setText("Reservando: " + estacaoNome);

        String[] horarios = {"Selecione o horário", "07:30 - 09:30", "09:30 - 11:30", "13:30 - 15:30", "18:00 - 19:00", "19:00 - 20:40"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, horarios);
        spinnerHorario.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        });

        btnConfirmarReserva.setOnClickListener(v -> confirmarReserva());
    }

    private void confirmarReserva() {
        String horarioStr = spinnerHorario.getSelectedItem().toString();
        String descricao = etDescricao.getText().toString();

        if (horarioStr.contains("Selecione")) {
            Toast.makeText(this, "Selecione um horário.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBarAgendamento.setVisibility(View.VISIBLE);

        try {
            String[] partes = horarioStr.split(" - ");
            String[] inicio = partes[0].split(":");
            String[] fim = partes[1].split(":");
            int minInicio = (Integer.parseInt(inicio[0]) * 60) + Integer.parseInt(inicio[1]);
            int minFim = (Integer.parseInt(fim[0]) * 60) + Integer.parseInt(fim[1]);

            String novoId = ReservaRepository.getInstance().gerarNovoId();
            Reserva nova = new Reserva(novoId, estacaoId, selectedDate, minInicio, minFim, descricao, "user_padrao");

            boolean sucesso = reservaService.fazerReserva(nova);

            progressBarAgendamento.setVisibility(View.GONE);

            if (sucesso) {
                Toast.makeText(this, "Estação reservada com sucesso!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Conflito: Estação ou Laboratório ocupados!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            progressBarAgendamento.setVisibility(View.GONE);
            Toast.makeText(this, "Erro.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}