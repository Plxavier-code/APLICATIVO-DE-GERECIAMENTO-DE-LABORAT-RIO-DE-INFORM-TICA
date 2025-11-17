package com.example.a2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// 1. Nome da classe
public class AgendamentoEstacaoActivity extends AppCompatActivity {

    private static final String TAG = "AgendamentoEstacao";

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Componentes da UI
    private Toolbar toolbarAgendamento;
    private TextView tvNomeEstacaoAgendamento; // Variável renomeada
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private EditText etDescricao;
    private Button btnConfirmarReserva;
    private ProgressBar progressBarAgendamento;

    // Dados da Reserva
    private String estacaoId;   // Variável renomeada
    private String estacaoNome; // Variável renomeada
    private String selectedDate;
    private String selectedTimeBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 2. Layout corrigido
        setContentView(R.layout.activity_agendamento_estacao);

        // Inicializa Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // 3. Recebe os dados da Intent com os nomes corretos
        estacaoId = getIntent().getStringExtra("ESTACAO_ID");
        estacaoNome = getIntent().getStringExtra("ESTACAO_NOME");

        if (currentUser == null || estacaoId == null) {
            Toast.makeText(this, "Erro: Usuário ou Estação não identificado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // --- Configuração da UI ---
        toolbarAgendamento = findViewById(R.id.toolbarAgendamento);
        setSupportActionBar(toolbarAgendamento);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // 4. Encontra o ID correto (que agora existe no XML)
        tvNomeEstacaoAgendamento = findViewById(R.id.tvNomeEstacaoAgendamento);
        calendarView = findViewById(R.id.calendarView);
        spinnerHorario = findViewById(R.id.spinnerHorario);
        etDescricao = findViewById(R.id.etDescricao);
        btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);
        progressBarAgendamento = findViewById(R.id.progressBarAgendamento);

        // Preenche o nome da estação
        tvNomeEstacaoAgendamento.setText("Reservando: " + estacaoNome);

        // Popula o Spinner de Horários
        String[] horarios = new String[]{
                "07:30 - 09:30", "09:30 - 11:30", "13:30 - 15:30",
                "15:30 - 17:30", "18:00 - 19:00", "19:00 - 20:40", "20:40 - 22:00"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, horarios);
        spinnerHorario.setAdapter(adapter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        });

        btnConfirmarReserva.setOnClickListener(v -> {
            selectedTimeBlock = spinnerHorario.getSelectedItem().toString();
            validarConflito();
        });
    }

    private void validarConflito() {
        showLoading(true);

        // 5. Busca na coleção de reservas de estações
        db.collection("reservas_estacoes")
                .whereEqualTo("estacao_id", estacaoId) // Chave corrigida
                .whereEqualTo("data_reserva_str", selectedDate)
                .whereEqualTo("horario_bloco", selectedTimeBlock)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            salvarReserva();
                        } else {
                            Toast.makeText(AgendamentoEstacaoActivity.this, "Horário indisponível. Já existe uma reserva.", Toast.LENGTH_LONG).show();
                            showLoading(false);
                        }
                    } else {
                        Toast.makeText(AgendamentoEstacaoActivity.this, "Erro ao verificar horários.", Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                });
    }

    private void salvarReserva() {
        String professorId = currentUser.getUid();
        String professorEmail = currentUser.getEmail();
        String descricao = etDescricao.getText().toString().trim();

        Date dataReserva = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dataReserva = sdf.parse(selectedDate);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao converter data", e);
            showLoading(false);
            return;
        }

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("professor_id", professorId);
        reserva.put("professor_email", professorEmail);
        reserva.put("estacao_id", estacaoId); // Chave corrigida
        reserva.put("nome_estacao", estacaoNome); // Chave corrigida
        reserva.put("data_reserva", dataReserva);
        reserva.put("data_reserva_str", selectedDate);
        reserva.put("horario_bloco", selectedTimeBlock);
        reserva.put("descricao", descricao.isEmpty() ? "Nenhuma descrição" : descricao);
        reserva.put("status", "Confirmada");
        reserva.put("data_criacao", new Date());

        // 6. Salva na coleção correta
        db.collection("reservas_estacoes")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AgendamentoEstacaoActivity.this, "Reserva de estação confirmada!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AgendamentoEstacaoActivity.this, "Erro ao salvar reserva.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBarAgendamento.setVisibility(View.VISIBLE);
            btnConfirmarReserva.setEnabled(false);
        } else {
            progressBarAgendamento.setVisibility(View.GONE);
            btnConfirmarReserva.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}