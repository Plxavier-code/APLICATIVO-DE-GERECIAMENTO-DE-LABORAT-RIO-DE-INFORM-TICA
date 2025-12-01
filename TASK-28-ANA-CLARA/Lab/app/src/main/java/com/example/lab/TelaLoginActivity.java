package com.example.lab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaLoginActivity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.username);
        campoSenha = findViewById(R.id.password);
        final Button botaoLogin = findViewById(R.id.login);
        final TextView botaoCadastro = findViewById(R.id.register);

        botaoLogin.setOnClickListener(v -> {
            String email = campoEmail.getText().toString();
            String senha = campoSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            RepositorioUsuario repositorioUsuario = RepositorioUsuario.getInstancia();
            Usuario usuario = repositorioUsuario.encontrarUsuarioPorEmail(email);

            if (usuario != null && usuario.getSenha().equals(senha)) {
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TelaLoginActivity.this, TelaPrincipalActivity.class);
                intent.putExtra("USER_PROFILE", usuario.getPerfil());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        botaoCadastro.setOnClickListener(v -> {
            startActivity(new Intent(TelaLoginActivity.this, TelaCadastroActivity.class));
        });
    }
}