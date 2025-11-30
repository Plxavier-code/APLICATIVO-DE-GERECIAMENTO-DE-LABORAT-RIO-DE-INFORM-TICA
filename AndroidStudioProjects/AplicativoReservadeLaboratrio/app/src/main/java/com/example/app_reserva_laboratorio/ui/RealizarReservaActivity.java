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

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Usuario;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.example.app_reserva_laboratorio.session.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class RealizarReservaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;
    private ImageButton backButton;
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

        reservaService = new ReservaService(this);

        labIdSelecionado = getIntent().getStringExtra("LAB_ID");
        if (labIdSelecionado == null || labIdSelecionado.isEmpty()) {
            Toast.makeText(this, "Erro: ID do laboratório não recebido.", Toast.LENGTH_LONG).show();
            labIdSelecionado = "lab_h401";
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupHeader();

        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        backButton.setOnClickListener(v -> finish());

        calendarView = findViewById(R.id.calendar_view);
        spinnerHorario = findViewById(R.id.spinner_horario);
        editTextDescricao = findViewById(R.id.edit_text_descricao);
        btnConfirmarReserva = findViewById(R.id.btn_confirmar_reserva);

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
                Toast.makeText(RealizarReservaActivity.this, "Selecione data e horário", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RealizarReservaActivity.this, "Reserva realizada com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RealizarReservaActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RealizarReservaActivity.this, "ERRO: Este horário já está reservado!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(RealizarReservaActivity.this, "Erro ao processar o horário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.nav_user_name);
        
        Usuario usuarioLogado = SessionManager.getInstance().getUsuarioLogado();
        if (usuarioLogado != null) {
            tvUserName.setText(usuarioLogado.getNome());
        } else {
            tvUserName.setText("Usuário não identificado");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_admin) {
            if (SessionManager.getInstance().isProfessor()) {
                startActivity(new Intent(this, AdminMainActivity.class));
            } else {
                Toast.makeText(this, "Acesso negado.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_sair) {
            SessionManager.getInstance().logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
