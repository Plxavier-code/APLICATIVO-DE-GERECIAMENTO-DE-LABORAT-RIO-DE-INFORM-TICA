package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Usuario;
import com.example.app_reserva_laboratorio.service.SessionManager;
import com.example.app_reserva_laboratorio.service.UsuarioService;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    private UsuarioService usuarioService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioService = new UsuarioService();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        new Handler().postDelayed(() -> {
            Usuario usuario = usuarioService.autenticar(email, password);

            if (usuario != null) {
                // Salva o usuário na sessão
                SessionManager.getInstance().login(usuario);

                // Redireciona com base no tipo de usuário
                Intent intent;
                if (usuario.getTipo() == Usuario.Tipo.ADMINISTRADOR) {
                    intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                } else {
                    // Professores e Alunos vão para a mesma tela principal
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish(); // Fecha a tela de login para não poder voltar
            } else {
                Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos", Toast.LENGTH_LONG).show();
            }
            showLoading(false);
        }, 1000); // Delay de 1 segundo para simular a autenticação
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
    }
}
