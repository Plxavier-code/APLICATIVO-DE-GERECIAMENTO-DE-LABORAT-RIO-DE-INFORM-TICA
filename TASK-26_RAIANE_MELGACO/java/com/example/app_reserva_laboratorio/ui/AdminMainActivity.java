package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.app_reserva_laboratorio.R;
import com.google.android.material.button.MaterialButton;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Painel do Administrador");
        }

        MaterialButton btnGerenciarLabs = findViewById(R.id.btn_admin_gerenciar_labs);
        MaterialButton btnGerenciarUsuarios = findViewById(R.id.btn_admin_gerenciar_usuarios);
        MaterialButton btnGerenciarSoftware = findViewById(R.id.btn_admin_gerenciar_software);
        MaterialButton btnConsultarSenhas = findViewById(R.id.btn_admin_consultar_senhas);
        MaterialButton btnModuloAluno = findViewById(R.id.btn_modulo_aluno);

        btnGerenciarLabs.setOnClickListener(v -> startActivity(new Intent(this, GerenciarLabsActivity.class)));
        btnGerenciarUsuarios.setOnClickListener(v -> startActivity(new Intent(this, GerenciarUsuariosActivity.class)));
        btnGerenciarSoftware.setOnClickListener(v -> startActivity(new Intent(this, GerenciarSoftwareActivity.class)));
        btnConsultarSenhas.setOnClickListener(v -> startActivity(new Intent(this, ConsultarSenhasActivity.class)));
        btnModuloAluno.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }
}
