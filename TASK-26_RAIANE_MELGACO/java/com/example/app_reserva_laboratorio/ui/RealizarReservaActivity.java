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

import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity responsável por permitir ao usuário realizar uma reserva geral
 * (não associada a uma estação específica).
 * Inclui seleção de data, horário, descrição e uso de Navigation Drawer.
 */
public class RealizarReservaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componentes do menu lateral (Navigation Drawer)
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;

    // Componentes principais da tela
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private TextInputEditText editTextDescricao;
    private Button btnConfirmarReserva;


    private ReservaService reservaService;
    private String dataSelecionada;
    private String labIdSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_reserva);

        // Inicializa o "Back-end"
        reservaService = new ReservaService();

        // Pega o ID do laboratório que foi clicado na tela anterior
        labIdSelecionado = getIntent().getStringExtra("LAB_ID");
        if (labIdSelecionado == null || labIdSelecionado.isEmpty()) {
            Toast.makeText(this, "Erro: ID do laboratório não recebido.", Toast.LENGTH_LONG).show();
            labIdSelecionado = "lab_h401";
        }

        // Configuração do Navigation Drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        // Lógica para os botões do cabeçalho
        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        // Abre o menu lateral ao clicar no botão de menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Fecha a Activity ao clicar no botão voltar
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Lógica dos componentes da tela
        calendarView = findViewById(R.id.calendar_view);
        spinnerHorario = findViewById(R.id.spinner_horario);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);

        // Configura o Spinner - Horário
        String[] horarios = new String[]{
                "Selecione o horário",
                "18:00 - 19:00",
                "19:00 - 20:00",
                "20:00 - 21:00",
                "21:00 - 22:00"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, horarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapter);

        // Listener para seleção de data no calendário
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(RealizarReservaActivity.this, "Data selecionada: " + dataSelecionada, Toast.LENGTH_SHORT).show();
            }
        });

        // Configura o clique do botão Confirmar Reserva
        btnConfirmarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horarioSelecionado = spinnerHorario.getSelectedItem().toString();
                String descricao = editTextDescricao.getText().toString();

                // Validações
                if (dataSelecionada == null) {
                    Toast.makeText(RealizarReservaActivity.this, "Por favor, selecione uma data", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (horarioSelecionado.equals("Selecione o horário")) {
                    Toast.makeText(RealizarReservaActivity.this, "Por favor, selecione um horário", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Dados "Chumbados"
                String labId = "lab_h401";
                String alunoId = "aluno_teste_123";

                try {
                    // Converter os dados da tela
                    // Pega o repositório para gerar um novo ID
                    ReservaRepository repository = ReservaRepository.getInstance();
                    String novoId = repository.getProximoIdReserva();

                    // Converte a string "19:00 - 20:00" em minutos
                    String[] partesHorario = horarioSelecionado.split(" - ");
                    String[] inicioSplit = partesHorario[0].split(":");
                    String[] fimSplit = partesHorario[1].split(":");

                    int minutoInicio = (Integer.parseInt(inicioSplit[0]) * 60) + Integer.parseInt(inicioSplit[1]);
                    int minutoFim = (Integer.parseInt(fimSplit[0]) * 60) + Integer.parseInt(fimSplit[1]);

                    // Montar o objeto reserva
                    Reserva novaReserva = new Reserva(
                            novoId,
                            labIdSelecionado,
                            dataSelecionada,
                            minutoInicio,
                            minutoFim,
                            descricao,
                            alunoId
                    );

                    // Chamar o service
                    boolean sucesso = reservaService.FazerReserva(novaReserva);

                    // Responder ao usuário
                    if (sucesso) {
                        Toast.makeText(RealizarReservaActivity.this, "Reserva realizada com sucesso!", Toast.LENGTH_LONG).show();
                        // Volta para a tela principal
                        Intent intent = new Intent(RealizarReservaActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Fecha esta tela
                    } else {
                        // Se o service retornou 'false', é porque deu conflito
                        Toast.makeText(RealizarReservaActivity.this, "ERRO: Este horário já está reservado!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // Caso algo dê errado na conversão do horário
                    Toast.makeText(RealizarReservaActivity.this, "Erro ao processar o horário.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Configuração do botão Voltar
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
            Intent intent = new Intent(RealizarReservaActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
