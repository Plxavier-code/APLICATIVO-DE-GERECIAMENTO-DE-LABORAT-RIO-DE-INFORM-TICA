package com.example.a2.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.a2.R;
import com.example.a2.data.Tipo;
import com.example.a2.data.Usuario;
import com.example.a2.service.SessionManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    // Launcher para o pedido de permissão
    private final ActivityResultLauncher<String> requestPermissionLauncher = 
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                Toast.makeText(this, "Permissão de notificação negada.", Toast.LENGTH_SHORT).show();
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pede a permissão de notificação
        pedirPermissaoDeNotificacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        MaterialButton btnFazerReserva = findViewById(R.id.btnFazerReserva);
        MaterialButton btnMinhasReservas = findViewById(R.id.btnMinhasReservas);

        Usuario usuarioLogado = SessionManager.getInstance().getUsuarioLogado();
        if (usuarioLogado != null) {
            tvWelcome.setText("Bem-vindo, " + usuarioLogado.getNome());
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Lógica de acesso por perfil
        btnFazerReserva.setOnClickListener(v -> {
            if (usuarioLogado.getTipo() == Tipo.PROFESSOR) {
                startActivity(new Intent(MainActivity.this, ListaLaboratoriosActivity.class));
            } else if (usuarioLogado.getTipo() == Tipo.ALUNO) {
                startActivity(new Intent(MainActivity.this, ListaEstacoesActivity.class));
            } else {
                Toast.makeText(this, "Apenas Professores e Alunos podem fazer reservas.", Toast.LENGTH_SHORT).show();
            }
        });

        btnMinhasReservas.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MinhasReservasActivity.class));
        });
    }

    private void pedirPermissaoDeNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
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
