package com.example.app_reserva_laboratorio.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Usuario;
import com.example.app_reserva_laboratorio.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private MaterialButton btnFazerReserva;
    private MaterialButton btnMinhasReservas;

    // Launcher para o pedido de permissão de notificação
    private final ActivityResultLauncher<String> requestPermissionLauncher = 
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "Permissão de notificação concedida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão de notificação negada.", Toast.LENGTH_SHORT).show();
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicita a permissão de notificação em tempo de execução
        pedirPermissaoDeNotificacao();

        // SIMULAÇÃO DE LOGIN
        simularLoginProfessor();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnFazerReserva = findViewById(R.id.btnFazerReserva);
        btnMinhasReservas = findViewById(R.id.btnMinhasReservas);

        btnFazerReserva.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NovaReservaActivity.class);
            startActivity(intent);
        });

        btnMinhasReservas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MinhasReservasActivity.class);
            startActivity(intent);
        });

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

    private void pedirPermissaoDeNotificacao() {
        // A permissão só é necessária para Android 13 (TIRAMISU) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Se a permissão não foi concedida, solicita ao usuário
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void simularLoginProfessor() {
        Usuario professor = ReservaRepository.getInstance().getUsuarios().stream()
                .filter(u -> "prof_teste_456".equals(u.getId()))
                .findFirst()
                .orElse(null);

        if (professor != null) {
            SessionManager.getInstance().login(professor);
        } else {
            Toast.makeText(this, "Erro: Professor de teste não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
            if (SessionManager.getInstance().isProfessor()) {
                startActivity(new Intent(this, AdminMainActivity.class));
            } else {
                Toast.makeText(this, "Acesso negado", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_sair) {
            SessionManager.getInstance().logout();
            Toast.makeText(this, "Sessão encerrada", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}