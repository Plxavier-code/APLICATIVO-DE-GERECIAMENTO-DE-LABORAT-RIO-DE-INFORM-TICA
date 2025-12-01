package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EditarUsuarioActivity extends AppCompatActivity {

    private TextInputEditText editTextNome;
    private TextInputEditText editTextEmail;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextNome = findViewById(R.id.edit_text_nome);
        editTextEmail = findViewById(R.id.edit_text_email);
        Button btnSalvar = findViewById(R.id.btn_salvar);

        String userId = getIntent().getStringExtra("USER_ID");
        usuario = getUsuarioById(userId);

        if (usuario != null) {
            editTextNome.setText(usuario.getNome());
            editTextEmail.setText(usuario.getEmail());
        }

        btnSalvar.setOnClickListener(v -> {
            String novoNome = editTextNome.getText().toString();
            String novoEmail = editTextEmail.getText().toString();

            if (usuario != null && !novoNome.isEmpty() && !novoEmail.isEmpty()) {
                usuario.setNome(novoNome);
                usuario.setEmail(novoEmail);
                // O ReservaRepository já tem a referência para o objeto, então a alteração é refletida
                Toast.makeText(this, "Usuário atualizado com sucesso", Toast.LENGTH_SHORT).show();
                finish(); // Volta para a tela anterior
            } else {
                Toast.makeText(this, "Nome e e-mail não podem ser vazios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Busca um usuário na lista completa do repositório pelo seu ID.
     */
    private Usuario getUsuarioById(String userId) {
        if (userId == null) return null;
        List<Usuario> usuarios = ReservaRepository.getInstance().getUsuarios();
        for (Usuario u : usuarios) {
            if (userId.equals(u.getId())) {
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Ação do botão voltar na toolbar
        return true;
    }
}
