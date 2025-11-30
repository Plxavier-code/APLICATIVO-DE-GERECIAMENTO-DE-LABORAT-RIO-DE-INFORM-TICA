package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.session.SessionManager;
import com.google.android.material.button.MaterialButton;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Encontra os botões
        MaterialButton btnGerenciarLabs = findViewById(R.id.btn_admin_gerenciar_labs);
        MaterialButton btnGerenciarUsuarios = findViewById(R.id.btn_admin_gerenciar_usuarios);
        MaterialButton btnGerenciarSoftware = findViewById(R.id.btn_admin_gerenciar_software);
        MaterialButton btnConsultarSenhas = findViewById(R.id.btn_admin_consultar_senhas);
        MaterialButton btnModuloAluno = findViewById(R.id.btn_modulo_aluno);
        MaterialButton btnLogout = findViewById(R.id.btn_logout); // Botão de Sair

        // Configura os cliques dos botões de navegação
        btnGerenciarLabs.setOnClickListener(v -> startActivity(new Intent(this, GerenciarLabsActivity.class)));
        btnGerenciarUsuarios.setOnClickListener(v -> startActivity(new Intent(this, GerenciarUsuariosActivity.class)));
        btnGerenciarSoftware.setOnClickListener(v -> startActivity(new Intent(this, GerenciarSoftwareActivity.class)));
        btnConsultarSenhas.setOnClickListener(v -> startActivity(new Intent(this, ConsultarSenhasActivity.class)));
        btnModuloAluno.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        // Configura a lógica de Logout
        btnLogout.setOnClickListener(v -> {
            // 1. Limpa a sessão do usuário
            SessionManager.getInstance().logout();

            // 2. Cria um Intent para a LoginActivity
            Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);

            // 3. Limpa a pilha de telas
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // 4. Inicia a tela de login
            startActivity(intent);
        });
    }
}
