package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity responsável por permitir que o usuário edite uma reserva de estação.
 * Contém menu lateral, seleção de data, horário, estação e campo de descrição.
 */
public class EditarEstacaoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componentes do Navigation Drawer (menu lateral)
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;

    // Componentes principais da tela
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private Spinner spinnerEstacao;
    private TextInputEditText editTextDescricao;
    private Button btnConfirmarReserva;
    private Button btnCancelarReserva;

    private ReservaService reservaService;
    private ReservaRepository reservaRepository;
    private Reserva reservaAtual; // Guarda a reserva que estamos editando
    private String dataSelecionada; // Guarda a nova data
    private String idReservaParaEditar; // O ID vindo da Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estacao);

        // Inicializa a lógica
        reservaService = new ReservaService(this);
        reservaRepository = ReservaRepository.getInstance();

        // Pega a reserva atual
        idReservaParaEditar = getIntent().getStringExtra("ID_RESERVA");
        if (idReservaParaEditar == null) {
            Toast.makeText(this, "Erro: ID da reserva não encontrado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        reservaAtual = reservaRepository.getReservaById(idReservaParaEditar);
        if (reservaAtual == null) {
            Toast.makeText(this, "Erro: Reserva não encontrada", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Configura a UI
        setupNavigationDrawer();
        setupSpinners();
        setupUIComponents();

        // Carrega os dados antigos
        preencherDadosAtuais();
    }
    /**
     * Carrega os dados da reserva atual nos campos da tela
     */
    private void preencherDadosAtuais() {
        editTextDescricao.setText(reservaAtual.getDescricao());
        dataSelecionada = reservaAtual.getData();

        // TODO: Pré-selecionar o calendário e os spinners
        Toast.makeText(this, "Por favor, re-selecione a estaçao, data e horário", Toast.LENGTH_SHORT).show();
    }

    /**
     * Configura os componentes da tela (botões, calendário)
     */
    private void setupUIComponents() {
        calendarView = findViewById(R.id.calendar_view);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);
        btnCancelarReserva = findViewById(R.id.btn_cancelar_reserva);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        // Lógica para confirmar a reserva
        btnConfirmarReserva.setOnClickListener(v -> {
            String novaEstacao = spinnerEstacao.getSelectedItem().toString();
            String novoHorario = spinnerHorario.getSelectedItem().toString();
            String novaDescricao = editTextDescricao.getText().toString();

            if (novoHorario.equals("Selecione o horário") || dataSelecionada == null || novaEstacao.equals("Selecione a estação")) {
                Toast.makeText(this, "Selecione estação, data e horário", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Converter horário para minutos
                String[] partesHorario = novoHorario.split(" - ");
                String[] inicioSplit = partesHorario[0].split(":");
                String[] fimSplit = partesHorario[1].split(":");
                int minIni = (Integer.parseInt(inicioSplit[0]) * 60) + Integer.parseInt(inicioSplit[1]);
                int minFim = (Integer.parseInt(fimSplit[0]) * 60) + Integer.parseInt(fimSplit[1]);

                // Chama o service para tentar editar
                boolean sucesso = reservaService.EditarReserva(
                        idReservaParaEditar,
                        dataSelecionada,
                        minIni,
                        minFim,
                        novaDescricao,
                        novaEstacao
                );

                if (sucesso) {
                    Toast.makeText(this, "Reserva atualizada com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "ERRO: Conflito de horário. Tente outro.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Erro ao processar horário.", Toast.LENGTH_SHORT).show();
            }
        });

        // Lógica para cancelar reserva
        btnCancelarReserva.setOnClickListener(v -> {
            reservaService.cancelarReserva(idReservaParaEditar);
            Toast.makeText(EditarEstacaoActivity.this, "Reserva cancelada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * Configura os Spinners de horários e estações
     */
    private void setupSpinners() {
        spinnerHorario = findViewById(R.id.spinner_horario);
        spinnerEstacao = findViewById(R.id.spinner_estacao);

        String[] estacoes = new String[]{
                "Selecione a estação", "Estação 12", "Estação 14", "Estação 15", "Estação 16"
        };
        ArrayAdapter<String> adapterEstacao = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estacoes);
        adapterEstacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstacao.setAdapter(adapterEstacao);

        String[] horarios = new String[]{
                "Selecione o horário", "18:00 - 19:00", "19:00 - 20:00", "21:00 - 22:00"
        };
        ArrayAdapter<String> adapterHorario = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, horarios);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapterHorario);
    }

    /**
     * Configura o menu lateral (Navigation Drawer) e botões de cabeçalho
     */
    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        backButton.setOnClickListener(v -> finish());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Ações ao clicar em um item do menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
