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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

public class RealizarEstacaoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;

    private CalendarView calendarView;
    private Spinner spinnerHorario;
    private TextInputEditText editTextDescricao;
    private Button btnConfirmarReserva;

    // NOVOS COMPONENTES
    private RecyclerView recyclerViewEstacoes;
    private EstacaoAdapter estacaoAdapter;

    private ReservaService reservaService;
    private String dataSelecionada;
    private String labIdSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_estacao);

        reservaService = new ReservaService();

        labIdSelecionado = getIntent().getStringExtra("LAB_ID");
        if (labIdSelecionado == null || labIdSelecionado.isEmpty()) {
            Toast.makeText(this, "Erro: ID do laboratório não recebido.", Toast.LENGTH_LONG).show();
            labIdSelecionado = "lab_h403"; // Fallback
        }

        setupNavigationDrawer();
        setupViews();
        setupHorarioSpinner();
        setupCalendar();
        setupEstacaoRecyclerView();
        setupConfirmButton();
    }

    private void setupViews() {
        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);
        calendarView = findViewById(R.id.calendar_view);
        spinnerHorario = findViewById(R.id.spinner_horario);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);
        recyclerViewEstacoes = findViewById(R.id.recycler_view_estacoes);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        backButton.setOnClickListener(v -> finish());
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);
    }

    private void setupEstacaoRecyclerView() {
        // 1. Encontra o nome do laboratório pai
        Laboratorio labPai = ReservaRepository.getInstance().getLaboratorioById(labIdSelecionado);
        if (labPai == null) {
            Toast.makeText(this, "Laboratório principal não encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView textLabNome = findViewById(R.id.text_lab_nome);
        textLabNome.setText("Reservar Estação em: " + labPai.getNome());

        // 2. Filtra a lista para pegar apenas as estações daquele laboratório
        List<Laboratorio> todasAsEstacoes = ReservaRepository.getInstance().getLaboratorios();
        List<Laboratorio> estacoesDoLab = todasAsEstacoes.stream()
                .filter(l -> l.getNome().startsWith("Estação") && l.getNome().contains(labPai.getNome()))
                .collect(Collectors.toList());

        // 3. Configura o RecyclerView e o Adapter
        recyclerViewEstacoes.setLayoutManager(new GridLayoutManager(this, 4)); // Exibe 4 colunas
        estacaoAdapter = new EstacaoAdapter(this, estacoesDoLab);
        recyclerViewEstacoes.setAdapter(estacaoAdapter);
    }

    private void setupHorarioSpinner() {
        String[] horarios = {"Selecione o horário", "18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00"};
        ArrayAdapter<String> adapterHorario = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horarios);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapterHorario);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dataSelecionada = dayOfMonth + "/" + (month + 1) + "/" + year;
        });
    }

    private void setupConfirmButton() {
        btnConfirmarReserva.setOnClickListener(v -> {
            String horarioSelecionado = spinnerHorario.getSelectedItem().toString();
            String descricao = editTextDescricao.getText().toString();
            Laboratorio estacaoSelecionada = estacaoAdapter.getEstacaoSelecionada();

            // Validações
            if (dataSelecionada == null || dataSelecionada.isEmpty()) {
                Toast.makeText(this, "Por favor, selecione uma data", Toast.LENGTH_SHORT).show();
                return;
            }
            if (estacaoSelecionada == null) {
                Toast.makeText(this, "Por favor, selecione uma estação", Toast.LENGTH_SHORT).show();
                return;
            }
            if (horarioSelecionado.equals("Selecione o horário")) {
                Toast.makeText(this, "Por favor, selecione um horário", Toast.LENGTH_SHORT).show();
                return;
            }

            String alunoId = "aluno_teste_123";

            try {
                String novoId = ReservaRepository.getInstance().getProximoIdReserva();

                String[] partesHorario = horarioSelecionado.split(" - ");
                String[] inicioSplit = partesHorario[0].split(":");
                String[] fimSplit = partesHorario[1].split(":");
                int minutoInicio = (Integer.parseInt(inicioSplit[0]) * 60) + Integer.parseInt(inicioSplit[1]);
                int minutoFim = (Integer.parseInt(fimSplit[0]) * 60) + Integer.parseInt(fimSplit[1]);

                Reserva novaReserva = new Reserva(novoId, estacaoSelecionada.getId(), dataSelecionada, minutoInicio, minutoFim, descricao, alunoId);

                boolean sucesso = reservaService.FazerReserva(novaReserva);

                if (sucesso) {
                    Toast.makeText(this, "Reserva de estação realizada com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "ERRO: Este horário/estação já está reservado!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao processar a reserva.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}