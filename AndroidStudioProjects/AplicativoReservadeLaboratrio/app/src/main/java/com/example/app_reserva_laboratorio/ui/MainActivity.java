package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;

import com.example.app_reserva_laboratorio.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

/**
 * MainActivity é a tela principal (Home) do aplicativo.
 * Exibe os botões de acesso rápido ("Fazer Reserva" e "Minhas Reservas")
 * e controla o Navigation Drawer (menu lateral).
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Componete do menu lateral
    private DrawerLayout drawerLayout;
    // Componentes principais da tela
    private MaterialButton btnFazerReserva;
    private MaterialButton btnMinhasReservas;

    /**
     * Método onCreate é chamado quando a Activity é criada pela primeira vez.
     * É aqui que inicializamos o layout e os componentes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define qual arquivo XML de layout esta classe vai controlar
        setContentView(R.layout.activity_main);

        // Configuração da Toolbar (barra do topo)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configuração do Navigation Drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        // Cria o botão menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Conecta o menu lateral ao botão do topo
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Lógica dos Botões da Tela Principal
        btnFazerReserva = findViewById(R.id.btnFazerReserva);
        btnMinhasReservas = findViewById(R.id.btnMinhasReservas);

        // Abre a tela de criação de nova reserva
        btnFazerReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NovaReservaActivity.class);
                startActivity(intent);
            }
        });

        // Abre a tela com as reservas do usuário
        btnMinhasReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MinhasReservasActivity.class);
                startActivity(intent);
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

    /* Método onNavigationItemSelected é chamado quando um item no menu de navegação
     * (Navigation Drawer) é selecionado.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Obtenha o ID do item clicado
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
            Toast.makeText(this, "Clicou em Reservas", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Fecha o menu lateral após o clique
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
