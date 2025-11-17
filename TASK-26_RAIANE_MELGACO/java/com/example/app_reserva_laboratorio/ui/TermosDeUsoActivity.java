package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.app_reserva_laboratorio.R;
import com.google.android.material.navigation.NavigationView;

public class TermosDeUsoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos_de_uso);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        CheckBox checkBoxTermos = findViewById(R.id.checkbox_termos);
        Button btnContinuar = findViewById(R.id.btn_continuar);

        checkBoxTermos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnContinuar.setEnabled(true);
                btnContinuar.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde do tema
            } else {
                btnContinuar.setEnabled(false);
                btnContinuar.setBackgroundColor(Color.parseColor("#D9D9D9"));
            }
        });

        btnContinuar.setOnClickListener(v -> {
            Intent intent = new Intent(TermosDeUsoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_admin) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        }
        // Adicione aqui outras lógicas de menu se necessário

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
