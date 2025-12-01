package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_reserva_laboratorio.R;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MaterialButton btnFilaEspera = findViewById(R.id.btn_fila_espera);
        MaterialButton btnTaxaOcupacao = findViewById(R.id.btn_taxa_ocupacao);
        MaterialButton btnHistoricoManutencoes = findViewById(R.id.btn_historico_manutencoes);
        MaterialButton btnTaxaManutencaoPreventiva = findViewById(R.id.btn_taxa_manutencao_preventiva);

        btnFilaEspera.setOnClickListener(v -> {
            Toast.makeText(this, "Fila de Espera - A ser implementado", Toast.LENGTH_SHORT).show();
        });

        btnTaxaOcupacao.setOnClickListener(v -> {
            Toast.makeText(this, "Taxa de Ocupação - A ser implementado", Toast.LENGTH_SHORT).show();
        });

        btnHistoricoManutencoes.setOnClickListener(v -> {
            Toast.makeText(this, "Histórico de Manutenções - A ser implementado", Toast.LENGTH_SHORT).show();
        });

        btnTaxaManutencaoPreventiva.setOnClickListener(v -> {
            Toast.makeText(this, "Taxa de Manutenção Preventiva - A ser implementado", Toast.LENGTH_SHORT).show();
        });
    }
}
