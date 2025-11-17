package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaLabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_labs);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_labs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Laboratorio> todosOsLabs = ReservaRepository.getInstance().getLaboratorios();

        List<Laboratorio> labsDisponiveis = todosOsLabs.stream()
                .filter(lab -> lab.isDisponivel() && !lab.isEmManutencao() && !lab.getId().startsWith("est_"))
                .collect(Collectors.toList());

        LabsAdapter adapter = new LabsAdapter(this, labsDisponiveis);
        recyclerView.setAdapter(adapter);
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
        } else if (id == R.id.nav_admin) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
