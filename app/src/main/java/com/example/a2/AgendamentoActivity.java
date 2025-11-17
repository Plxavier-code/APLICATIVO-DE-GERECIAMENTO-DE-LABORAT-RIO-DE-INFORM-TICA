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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// VERSÃO DE DEBUG
public class AgendamentoActivity extends AppCompatActivity {

    private static final String TAG = "AgendamentoActivity";

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Componentes da UI
    private Toolbar toolbarAgendamento;
    private TextView tvNomeLabAgendamento;
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private EditText etDescricao;
    private Button btnConfirmarReserva;
    private ProgressBar progressBarAgendamento;

    // Dados da Reserva
    private String labId;
    private String labNome;
    private String selectedDate;
    private String selectedTimeBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- DEBUG 1 ---
        Toast.makeText(this, "PASSO 1: Iniciou onCreate()", Toast.LENGTH_SHORT).show();

        try {
            setContentView(R.layout.activity_agendamento);

            // --- DEBUG 2 ---
            Toast.makeText(this, "PASSO 2: Layout Carregado", Toast.LENGTH_SHORT).show();

            // Inicializa Firebase
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            // Recebe os dados da Intent (da tela anterior)
            labId = getIntent().getStringExtra("LABORATORIO_ID");
            labNome = getIntent().getStringExtra("LABORATORIO_NOME");

            if (currentUser == null || labId == null) {
                Toast.makeText(this, "Erro: Usuário ou Laboratório não identificado.", Toast.LENGTH_LONG).show();
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

            tvNomeLabAgendamento = findViewById(R.id.tvNomeLabAgendamento);
            calendarView = findViewById(R.id.calendarView);
            spinnerHorario = findViewById(R.id.spinnerHorario);
            etDescricao = findViewById(R.id.etDescricao);
            btnConfirmarReserva = findViewById(R.id.btnConfirmarReserva);
            progressBarAgendamento = findViewById(R.id.progressBarAgendamento);

            // --- DEBUG 3 ---
            Toast.makeText(this, "PASSO 3: Componentes Vinculados", Toast.LENGTH_SHORT).show();

            // Preenche o nome do laboratório
            tvNomeLabAgendamento.setText("Reservando: " + labNome);

            // Popula o Spinner de Horários (ADGLI-5)
            String[] horarios = new String[]{
                    "07:30 - 09:30",
                    "09:30 - 11:30",
                    "13:30 - 15:30",
                    "15:30 - 17:30",
                    "18:00 - 19:00", // Como na sua imagem
                    "19:00 - 20:40",
                    "20:40 - 22:00"
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, horarios);
            spinnerHorario.setAdapter(adapter);

            // --- DEBUG 4 ---
            Toast.makeText(this, "PASSO 4: Spinner Configurado", Toast.LENGTH_SHORT).show();

            // Salva a data selecionada
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate = sdf.format(new Date(calendarView.getDate()));

            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            });

            // Configura o clique do botão "Confirmar"
            btnConfirmarReserva.setOnClickListener(v -> {
                selectedTimeBlock = spinnerHorario.getSelectedItem().toString();
                validarConflito(); // ADGLI-6
            });

            // --- DEBUG 5 ---
            Toast.makeText(this, "PASSO 5: onCreate() Concluído!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "CRASH NO ONCREATE", e);
            // Se falhar, mostra a mensagem de erro exata
            Toast.makeText(this, "CRASH: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Fecha a tela que falhou
        }
    }

    // O resto do código (validarConflito, salvarReserva, etc.) é o mesmo
    // ...
    // ... (Omitido para ser breve, não precisa de copiar o resto se já o tem)
    // ...
    // Se o seu ficheiro estiver vazio, copie o resto do código da minha mensagem anterior
    // (a que começa com "private void validarConflito()") e cole-o aqui em baixo.

    /**
     * PASSO 1: Verifica se o horário está livre (ADGLI-6)
     */
    private void validarConflito() {
        showLoading(true);

        // Query: Procura por uma reserva com o MESMO lab, na MESMA data, no MESMO bloco de horário
        // **IMPORTANTE**: Crie a coleção "reservas" no seu Firebase
        db.collection("reservas")
                .whereEqualTo("laboratorio_id", labId)
                .whereEqualTo("data_reserva_str", selectedDate) // Usamos a string da data para busca
                .whereEqualTo("horario_bloco", selectedTimeBlock)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // NENHUM CONFLITO! O horário está livre.
                            Log.d(TAG, "Horário livre. Salvando reserva...");
                            salvarReserva();
                        } else {
                            // CONFLITO! Já existe uma reserva.
                            Log.w(TAG, "Conflito de reserva detectado.");
                            Toast.makeText(AgendamentoActivity.this, "Horário indisponível. Já existe uma reserva.", Toast.LENGTH_LONG).show();
                            showLoading(false);
                        }
                    } else {
                        Log.w(TAG, "Erro ao verificar conflitos.", task.getException());
                        Toast.makeText(AgendamentoActivity.this, "Erro ao verificar horários.", Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                });
    }

    /**
     * PASSO 2: Salva a reserva no Firestore
     */
    private void salvarReserva() {
        String professorId = currentUser.getUid();
        String professorEmail = currentUser.getEmail();
        String descricao = etDescricao.getText().toString().trim();

        // Converte a data string (ex: 16/11/2025) para um Timestamp do Firebase
        Date dataReserva = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dataReserva = sdf.parse(selectedDate);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao converter data", e);
            showLoading(false);
            return;
        }

        // Cria o objeto (Map) da reserva
        Map<String, Object> reserva = new HashMap<>();
        reserva.put("professor_id", professorId);
        reserva.put("professor_email", professorEmail);
        reserva.put("laboratorio_id", labId);
        reserva.put("nome_laboratorio", labNome);
        reserva.put("data_reserva", dataReserva);
        reserva.put("data_reserva_str", selectedDate);
        reserva.put("horario_bloco", selectedTimeBlock);
        reserva.put("descricao", descricao.isEmpty() ? "Nenhuma descrição" : descricao);
        reserva.put("status", "Confirmada");
        reserva.put("data_criacao", new Date());

        // Salva na coleção "reservas"
        db.collection("reservas")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    // SUCESSO! (ADGLI-3)
                    Log.d(TAG, "Reserva salva com ID: " + documentReference.getId());
                    Toast.makeText(AgendamentoActivity.this, "Reserva confirmada com sucesso!", Toast.LENGTH_LONG).show();
                    showLoading(false);

                    // Fecha a tela de agendamento e volta para a lista
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erro ao salvar reserva", e);
                    Toast.makeText(AgendamentoActivity.this, "Erro ao salvar reserva.", Toast.LENGTH_SHORT).show();
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