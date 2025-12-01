package com.example.lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Perfil perfilUsuario = (Perfil) getIntent().getSerializableExtra("USER_PROFILE");

        if (perfilUsuario == null) {
            finish();
            return;
        }

        configurarInterface(perfilUsuario);
    }

    private void configurarInterface(Perfil perfil) {
        Button gerenciarInventario = findViewById(R.id.btn_manage_inventory);
        Button registrarManutencao = findViewById(R.id.btn_register_maintenance);
        Button gerenciarUsuarios = findViewById(R.id.btn_manage_users);
        Button gerarRelatorios = findViewById(R.id.btn_generate_reports);

        Button reservarEstacao = findViewById(R.id.btn_reserve_station);
        Button reservarLaboratorio = findViewById(R.id.btn_reserve_lab);
        Button entrarFilaEspera = findViewById(R.id.btn_enter_queue);
        Button abrirChamadoSuporte = findViewById(R.id.btn_open_support_ticket);
        Button solicitarSoftware = findViewById(R.id.btn_request_software);

        switch (perfil) {
            case ADMINISTRADOR:
                gerenciarInventario.setVisibility(View.VISIBLE);
                registrarManutencao.setVisibility(View.VISIBLE);
                gerenciarUsuarios.setVisibility(View.VISIBLE);
                gerarRelatorios.setVisibility(View.VISIBLE);
                break;
            case PROFESSOR:
                reservarEstacao.setVisibility(View.VISIBLE);
                reservarLaboratorio.setVisibility(View.VISIBLE);
                entrarFilaEspera.setVisibility(View.VISIBLE);
                abrirChamadoSuporte.setVisibility(View.VISIBLE);
                solicitarSoftware.setVisibility(View.VISIBLE);
                break;
            case ALUNO:
                reservarEstacao.setVisibility(View.VISIBLE);
                entrarFilaEspera.setVisibility(View.VISIBLE);
                abrirChamadoSuporte.setVisibility(View.VISIBLE);
                solicitarSoftware.setVisibility(View.VISIBLE);
                break;
        }
    }
}
