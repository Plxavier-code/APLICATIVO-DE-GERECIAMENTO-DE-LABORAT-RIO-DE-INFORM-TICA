package com.example.lab;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastroActivity extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Spinner seletorPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        campoNome = findViewById(R.id.name);
        campoEmail = findViewById(R.id.username);
        campoSenha = findViewById(R.id.password);
        seletorPerfil = findViewById(R.id.profile_spinner);
        final Button botaoCadastrar = findViewById(R.id.register);
        final TextView botaoLogin = findViewById(R.id.login);

        ArrayAdapter<Perfil> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Perfil.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seletorPerfil.setAdapter(adapter);

        botaoCadastrar.setOnClickListener(v -> {
            String nome = campoNome.getText().toString();
            String email = campoEmail.getText().toString();
            String senha = campoSenha.getText().toString();
            Perfil perfilSelecionado = (Perfil) seletorPerfil.getSelectedItem();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Critério de segurança para criar administrador
            if (perfilSelecionado == Perfil.ADMINISTRADOR && !email.endsWith("@admin.com")) {
                Toast.makeText(this, "Email inválido para cadastro de administrador.", Toast.LENGTH_LONG).show();
                return;
            }

            Usuario novoUsuario = new Usuario(nome, email, senha, perfilSelecionado);
            RepositorioUsuario.getInstancia().adicionarUsuario(novoUsuario);

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        botaoLogin.setOnClickListener(v -> {
            finish();
        });
    }
}
