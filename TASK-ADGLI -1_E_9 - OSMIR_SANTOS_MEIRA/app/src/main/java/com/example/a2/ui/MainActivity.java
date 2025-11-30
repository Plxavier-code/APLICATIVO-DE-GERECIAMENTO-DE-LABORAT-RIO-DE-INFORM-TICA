package com.example.a2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.example.a2.data.Usuario;
import com.example.a2.service.SessionManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        MaterialButton btnFazerReserva = findViewById(R.id.btnFazerReserva);
        MaterialButton btnMinhasReservas = findViewById(R.id.btnMinhasReservas);

        Usuario usuarioLogado = SessionManager.getInstance().getUsuarioLogado();
        if (usuarioLogado != null) {
            tvWelcome.setText("Bem-vindo, " + usuarioLogado.getNome());
        } else {
            // Fallback se não houver usuário logado (não deveria acontecer)
            tvWelcome.setText("Bem-vindo!");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        btnFazerReserva.setOnClickListener(v -> {
            // A lógica de qual tela abrir pode ser mais complexa aqui
            // Por simplicidade, vamos para a lista de laboratórios.
            startActivity(new Intent(MainActivity.this, ListaLaboratoriosActivity.class));
        });

        btnMinhasReservas.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MinhasReservasActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            SessionManager.getInstance().logout();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
