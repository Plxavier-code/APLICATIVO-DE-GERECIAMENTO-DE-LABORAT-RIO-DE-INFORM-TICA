package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MinhasReservasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReservasAdapter.OnReservaClickListener {
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private ReservaService reservaService;
    private ReservaRepository reservaRepository;
    private ReservasAdapter adapter;
    private List<Reserva> minhasReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        reservaService = new ReservaService();
        reservaRepository = ReservaRepository.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view_reservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String alunoId = "aluno_teste_123";
        minhasReservas = reservaService.getMinhasReservas(alunoId);

        adapter = new ReservasAdapter(minhasReservas, this, reservaRepository);
        recyclerView.setAdapter(adapter);

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

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            String alunoId = "aluno_teste_123";
            List<Reserva> listaAtualizada = reservaService.getMinhasReservas(alunoId);
            minhasReservas.clear();
            minhasReservas.addAll(listaAtualizada);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditClick(Reserva reserva) {
        String idReserva = reserva.getIdReserva();
        String labId = reserva.getLaboratorioId();
        Intent intent;

        if (labId != null && labId.startsWith("est_")) {
            intent = new Intent(this, EditarEstacaoActivity.class);
            intent.putExtra("ID_RESERVA", idReserva);
        } else {
            intent = new Intent(this, EditarReservaActivity.class);
            intent.putExtra("ID_RESERVA", idReserva);
        }
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
