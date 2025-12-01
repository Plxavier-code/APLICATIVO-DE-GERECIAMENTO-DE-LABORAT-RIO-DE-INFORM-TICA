package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_reserva_laboratorio.R;
import com.google.android.material.button.MaterialButton;

public class ManutencaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao);

        MaterialButton btnManutencaoLaboratorio = findViewById(R.id.btnManutencaoLaboratorio);
        MaterialButton btnManutencaoEstacao = findViewById(R.id.btnManutencaoEstacao);

        btnManutencaoLaboratorio.setOnClickListener(v -> {
            Intent intent = new Intent(ManutencaoActivity.this, ListaLabsActivity.class);
            intent.putExtra("fluxo", "manutencao_laboratorio");
            startActivity(intent);
        });

        btnManutencaoEstacao.setOnClickListener(v -> {
            Intent intent = new Intent(ManutencaoActivity.this, ListaLabsActivity.class);
            intent.putExtra("fluxo", "manutencao_estacao");
            startActivity(intent);
        });
    }
}
