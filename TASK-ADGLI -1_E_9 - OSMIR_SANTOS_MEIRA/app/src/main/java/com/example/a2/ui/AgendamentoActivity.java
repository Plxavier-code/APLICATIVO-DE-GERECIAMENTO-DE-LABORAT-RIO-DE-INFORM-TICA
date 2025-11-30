package com.example.a2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.example.a2.data.Reserva;
import com.example.a2.data.ReservaRepository;
import com.example.a2.data.Usuario;
import com.example.a2.service.NotificationService;
import com.example.a2.service.ReservaService;
import com.example.a2.service.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgendamentoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private TextInputEditText editTextDescricao;
    private Button btnConfirmarReserva;
    private ReservaService reservaService;
    private NotificationService notificationService;
    private String dataSelecionada;
    private String labIdSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        reservaService = new ReservaService();
        notificationService = new NotificationService(this);
        labIdSelecionado = getIntent().getStringExtra("LAB_ID");

        if (labIdSelecionado == null || labIdSelecionado.isEmpty()) {
            Toast.makeText(this, "Erro: ID do laboratório não recebido.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agendar Horário");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendar_view);
        spinnerHorario = findViewById(R.id.spinner_horario);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);

        // Define a data inicial como a data atual
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataSelecionada = sdf.format(cal.getTime());

        String[] horarios = {"Selecione o horário", "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        btnConfirmarReserva.setOnClickListener(v -> {
            String horarioSelecionado = spinnerHorario.getSelectedItem().toString();
            String descricao = editTextDescricao.getText().toString();

            if (dataSelecionada == null || horarioSelecionado.equals("Selecione o horário")) {
                Toast.makeText(AgendamentoActivity.this, "Selecione data e horário", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
            if (usuario == null) {
                Toast.makeText(this, "Erro de sessão. Faça login novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                ReservaRepository repository = ReservaRepository.getInstance();
                String novoId = repository.getProximoIdReserva();

                String[] partesHorario = horarioSelecionado.split(" - ");
                String[] inicioSplit = partesHorario[0].split(":");
                String[] fimSplit = partesHorario[1].split(":");
                int minutoInicio = (Integer.parseInt(inicioSplit[0]) * 60) + Integer.parseInt(inicioSplit[1]);
                int minutoFim = (Integer.parseInt(fimSplit[0]) * 60) + Integer.parseInt(fimSplit[1]);

                Reserva novaReserva = new Reserva(novoId, labIdSelecionado, dataSelecionada, minutoInicio, minutoFim, descricao, usuario.getId());

                boolean sucesso = reservaService.FazerReserva(novaReserva);

                if (sucesso) {
                    Toast.makeText(AgendamentoActivity.this, "Reserva realizada com sucesso!", Toast.LENGTH_LONG).show();
                    notificationService.sendNotification("Nova Reserva de Laboratório", "O laboratório " + labIdSelecionado + " foi reservado.");
                    Intent intent = new Intent(AgendamentoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AgendamentoActivity.this, "ERRO: Este horário já está reservado!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(AgendamentoActivity.this, "Erro ao processar o horário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
