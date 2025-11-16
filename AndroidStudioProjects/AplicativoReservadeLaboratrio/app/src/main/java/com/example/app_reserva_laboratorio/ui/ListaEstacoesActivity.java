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

import com.example.app_reserva_laboratorio.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

/**
 * ListaEstacoesActivity exibe todas as estações disponíveis para reserva.
 * Cada card representa uma estação (H401, H402, H403, etc.).
 *
 * Contém menu lateral (Navigation Drawer), botão de menu e botão voltar no topo
 * e cards clicáveis que abrem a tela RealizarEstacaoActivity
 *
 * Ela funciona como uma lista intermediária antes da tela de agendamento
 */
public class ListaEstacoesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componentes do menu lateral
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;
    // Cada card representa uma estação clicável
    private MaterialCardView cardH401, cardH402, cardH403, cardH404, cardH406, cardH407;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_estacoes);

        // Configuração do Navigation Drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        // Lógica para os botões do cabeçalho
        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        // Abre o menu lateral ao clicar no botão de menu
        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Fecha a Activity ao clicar no botão voltar
        backButton.setOnClickListener(v -> finish());

        // Lógica de clique dos cards

        // Localiza os cards no layout
        cardH401 = findViewById(R.id.lab_h401);
        cardH402 = findViewById(R.id.lab_h402);
        cardH403 = findViewById(R.id.lab_h403);
        cardH404 = findViewById(R.id.lab_h404);
        cardH406 = findViewById(R.id.lab_h406);
        cardH407 = findViewById(R.id.lab_h407);

        // listener compartilhado que abre a tela RealizarEstacaoActivity
        View.OnClickListener estacaoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String labId = ""; // ID do laboratório PAI

                // Descobre qual card foi clicado
                int id = v.getId();
                if (id == R.id.lab_h401) {
                    labId = "lab_h401";
                } else if (id == R.id.lab_h402) {
                    labId = "lab_h402";
                } else if (id == R.id.lab_h403) {
                    labId = "lab_h403";
                } else if (id == R.id.lab_h404) {
                    labId = "lab_h404";
                } else if (id == R.id.lab_h406) {
                    labId = "lab_h406";
                } else if (id == R.id.lab_h407) {
                    labId = "lab_h407";
                }

                // Abre a tela de realizar reserva de estação
                Intent intent = new Intent(ListaEstacoesActivity.this, RealizarEstacaoActivity.class);
                // Envia o ID do laboratório pai para a próxima tela
                intent.putExtra("LAB_ID", labId);
                startActivity(intent);
            }
        };

        // Aplica o listener a todos os cards
        cardH401.setOnClickListener(estacaoClickListener);
        cardH402.setOnClickListener(estacaoClickListener);
        cardH403.setOnClickListener(estacaoClickListener);
        cardH404.setOnClickListener(estacaoClickListener);
        cardH406.setOnClickListener(estacaoClickListener);
        cardH407.setOnClickListener(estacaoClickListener);


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
            Intent intent = new Intent(ListaEstacoesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
