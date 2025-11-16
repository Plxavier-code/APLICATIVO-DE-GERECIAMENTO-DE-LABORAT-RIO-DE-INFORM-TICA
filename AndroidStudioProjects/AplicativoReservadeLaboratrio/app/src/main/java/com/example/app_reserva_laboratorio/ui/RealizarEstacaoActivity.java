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
 * Activity responsável por permitir ao usuário realizar uma reserva de estação
 * em um determinado horário, selecionando data, estação e uma descrição
 *
 * Contém menu lateral, calendário, spinners e campo de texto
 */
public class RealizarEstacaoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componentes do menu lateral
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;

    // Componentes principais da tela
    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private Spinner spinnerEstacao;
    private TextInputEditText editTextDescricao;
    private Button btnConfirmarReserva;

    private ReservaService reservaService;
    private String dataSelecionada;
    private String labIdSelecionado;

    /**
     * Método onCreate é chamado quando a Activity é criada pela primeira vez.
     * É aqui que inicializamos o layout e os componentes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_estacao);

        // Inicializa o Back-end
        reservaService = new ReservaService();

        // Pega o ID do laboratório que foi clicado na tela anterior
        labIdSelecionado = getIntent().getStringExtra("LAB_ID");
        if (labIdSelecionado == null || labIdSelecionado.isEmpty()) {
            Toast.makeText(this, "Erro: ID do laboratório não recebido.", Toast.LENGTH_LONG).show();
            labIdSelecionado = "lab_h403"; // Usa H403 como padrão em caso de erro
        }

        // Configuração do Navigation Drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Define o listener para clicar nos itens do menu lateral
        navigationView.setNavigationItemSelectedListener(this);
        // Deixa o item "Reservas" como selecionado
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
        spinnerEstacao = findViewById(R.id.spinner_estacao);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);

        // Configuração dos Spinners

        // Lista de estações - agora dinâmica
        String[] estacoes;
        String labPaiNome = "";

        switch (labIdSelecionado) {
            case "lab_h401":
                labPaiNome = "LAB - H401";
                break;
            case "lab_h402":
                labPaiNome = "LAB - H402";
                break;
            case "lab_h404":
                labPaiNome = "LAB - H404";
                break;
            case "lab_h406":
                labPaiNome = "LAB - H406";
                break;
            case "lab_h407":
                labPaiNome = "LAB - H407";
                break;
            case "lab_h403":
            default:
                labPaiNome = "LAB - H403";
                break;
        }

        // Define o array de strings com base no nome do Lab Pai
        estacoes = new String[]{
                "Selecione a estação",
                "Estação 1 - " + labPaiNome,
                "Estação 5 - " + labPaiNome,
                "Estação 10 - " + labPaiNome,
                "Estação 12 - " + labPaiNome
        };

        // Adapter que preenche o dropdown com as estações
        ArrayAdapter<String> adapterEstacao = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estacoes);
        adapterEstacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstacao.setAdapter(adapterEstacao);

        // Lista de horários
        String[] horarios = new String[]{
                "Selecione o horário",
                "18:00 - 19:00",
                "19:00 - 20:00",
                "20:00 - 21:00",
                "21:00 - 22:00"
        };
        // Adapter que preenche o dropdown com os horários
        ArrayAdapter<String> adapterHorario = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, horarios);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapterHorario);

        // Listener para seleção de data no calendário
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(RealizarEstacaoActivity.this, "Data selecionada: " + dataSelecionada, Toast.LENGTH_SHORT).show();
            }
        });

        // Configura o clique do botão Confirmar Reserva
        btnConfirmarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura os valores selecionados pelo usuário
                String estacaoSelecionada = spinnerEstacao.getSelectedItem().toString();
                String horarioSelecionado = spinnerHorario.getSelectedItem().toString();
                String descricao = editTextDescricao.getText().toString();

                // Valida os dados
                if (dataSelecionada == null) {
                    Toast.makeText(RealizarEstacaoActivity.this, "Por favor, selecione uma data", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (horarioSelecionado.equals("Selecione o horário")) {
                    Toast.makeText(RealizarEstacaoActivity.this, "Por favor, selecione um horário", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (estacaoSelecionada.equals("Selecione a estação")) {
                    Toast.makeText(RealizarEstacaoActivity.this, "Por favor, selecione uma estação", Toast.LENGTH_SHORT).show();
                    return;
                }

                String alunoId = "aluno_teste_123";

                try {
                    // Converter os dados da Tela

                    // Traduzir o nome do Spinner para o ID do Repositório
                    String prefixoId = labIdSelecionado.replace("lab_", "est_");

                    String estacaoId = "";
                    if (estacaoSelecionada.startsWith("Estação 1 ")) {
                        estacaoId = prefixoId + "_1";
                    } else if (estacaoSelecionada.startsWith("Estação 5 ")) {
                        estacaoId = prefixoId + "_5";
                    } else if (estacaoSelecionada.startsWith("Estação 10 ")) {
                        estacaoId = prefixoId + "_10";
                    } else if (estacaoSelecionada.startsWith("Estação 12 ")) {
                        estacaoId = prefixoId + "_12";
                    } else {
                        // Se não achar, usa o próprio texto
                        estacaoId = estacaoSelecionada;
                    }

                    // Pega o repositório para gerar um novo ID
                    ReservaRepository repository = ReservaRepository.getInstance();
                    String novoId = repository.getProximoIdReserva();

                    // Converte a string "19:00 - 20:00" em minutos
                    String[] partesHorario = horarioSelecionado.split(" - "); // ["19:00", "20:00"]
                    String[] inicioSplit = partesHorario[0].split(":"); // ["19", "00"]
                    String[] fimSplit = partesHorario[1].split(":");   // ["20", "00"]

                    int minutoInicio = (Integer.parseInt(inicioSplit[0]) * 60) + Integer.parseInt(inicioSplit[1]);
                    int minutoFim = (Integer.parseInt(fimSplit[0]) * 60) + Integer.parseInt(fimSplit[1]);

                    // Montar o objeto reserva
                    // A 'estacaoSelecionada' é o ID do laboratório/estação
                    Reserva novaReserva = new Reserva(
                            novoId,
                            estacaoId,
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
                        Toast.makeText(RealizarEstacaoActivity.this, "Reserva de estação realizada com sucesso!", Toast.LENGTH_LONG).show();
                        // Volta para a tela principal
                        Intent intent = new Intent(RealizarEstacaoActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Fecha esta tela
                    } else {
                        // Se o service retornou 'false', é porque deu conflito
                        Toast.makeText(RealizarEstacaoActivity.this, "ERRO: Este horário/estação já está reservado!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // Caso algo dê errado na conversão do horário
                    Toast.makeText(RealizarEstacaoActivity.this, "Erro ao processar o horário.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuração do botão Voltar
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Se o menu lateral estiver aberto, fecha
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Caso contrário, sai da Activity
                    finish();
                }
            }
        };
        // Ativa o callback
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Ações ao clicar em um item do menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Opção -> Perfil
        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        }
        // Opção -> Reservas
        else if (id == R.id.nav_reservas) {
            Intent intent = new Intent(RealizarEstacaoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        // Opção -> Sair
        else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
        }
        // Fecha o menu lateral depois da ação
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
