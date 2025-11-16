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
 * ListaLabsActivity exibe os laboratórios disponíveis para reserva.
 * Cada card representa um laboratório (H401, H402, H403, etc.) e ao clicar
 * em qualquer um deles o usuário é levado para a tela de criação de reserva
 * de laboratório (RealizarReservaActivity).
 */
public class ListaLabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componentes do menu lateral
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private ImageButton backButton;
    // Cards que representam os laboratórios
    private MaterialCardView cardH401, cardH402, cardH403, cardH404, cardH406, cardH407;

    // Método chamado quando a Activity é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_labs);

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

        // Lógica de clique dos cards

        // Localiza os cards no layout
        cardH401 = findViewById(R.id.lab_h401);
        cardH402 = findViewById(R.id.lab_h402);
        cardH403 = findViewById(R.id.lab_h403);
        cardH404 = findViewById(R.id.lab_h404);
        cardH406 = findViewById(R.id.lab_h406);
        cardH407 = findViewById(R.id.lab_h407);

        // listener compartilhado que abre a tela RealizarReservaActivity
        View.OnClickListener labClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String labId = ""; // ID do laboratório

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
                // Qualquer card clicado abre a tela de Realizar Reserva
                Intent intent = new Intent(ListaLabsActivity.this, RealizarReservaActivity.class);
                intent.putExtra("LAB_ID", labId);
                startActivity(intent);
            }
        };

        // Aplica o listener a todos os cards
        cardH401.setOnClickListener(labClickListener);
        cardH402.setOnClickListener(labClickListener);
        cardH403.setOnClickListener(labClickListener);
        cardH404.setOnClickListener(labClickListener);
        cardH406.setOnClickListener(labClickListener);
        cardH407.setOnClickListener(labClickListener);


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
            Intent intent = new Intent(ListaLabsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}