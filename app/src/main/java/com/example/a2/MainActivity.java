package com.example.a2; // Seu pacote

import androidx.appcompat.app.AlertDialog; // Importar AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context; // Importar Context
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater; // Importar LayoutInflater
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton; // Importar ImageButton
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    // Componentes da UI
    private Toolbar toolbar;
    private ProgressBar progressBarMain;
    private LinearLayout layoutReservas;
    private TextView tvWelcome, tvNoReservas;
    private FloatingActionButton fabNovaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            goToLogin();
            return;
        }

        // Vincula os componentes do layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBarMain = findViewById(R.id.progressBarMain);
        layoutReservas = findViewById(R.id.layoutReservas);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvNoReservas = findViewById(R.id.tvNoReservas);
        fabNovaReserva = findViewById(R.id.fabNovaReserva);

        // Personaliza a mensagem de boas-vindas
        tvWelcome.setText("Bem-vindo, " + (currentUser.getDisplayName() != null ? currentUser.getDisplayName() : currentUser.getEmail()));

        // Configura o clique do botão FAB (ADGLI-4)
        fabNovaReserva.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpcoesReservaActivity.class);
            startActivity(intent);
        });
    }

    /**
     * O onResume() é chamado SEMPRE que a tela volta a ficar visível.
     * Isto garante que a lista de reservas está sempre atualizada.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (currentUser != null) {
            loadReservas(); // <-- A CHAMADA CORRETA ESTÁ AQUI
        }
    }

    /**
     * Carrega as reservas do Firestore e as exibe na tela (ADGLI-104)
     * --- AGORA ATUALIZADO PARA USAR O NOVO LAYOUT DE CARD ---
     */
    private void loadReservas() {
        showLoading(true);
        layoutReservas.removeAllViews(); // Limpa a lista antes de carregar
        tvNoReservas.setVisibility(View.GONE);

        // Busca na coleção "reservas"
        db.collection("reservas")
                .whereEqualTo("professor_id", currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Nenhuma reserva encontrada
                            tvNoReservas.setVisibility(View.VISIBLE);
                        } else {
                            // Exibe cada reserva usando o novo layout de card
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                // "Infla" (cria) o layout do card (list_item_minha_reserva.xml)
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View reservaCard = inflater.inflate(R.layout.list_item_minha_reserva, layoutReservas, false);

                                // Pega os componentes DENTRO do card
                                TextView tvNome = reservaCard.findViewById(R.id.tvCardLabNome);
                                TextView tvData = reservaCard.findViewById(R.id.tvCardData);
                                TextView tvDescricao = reservaCard.findViewById(R.id.tvCardDescricao);
                                ImageButton btnCancelar = reservaCard.findViewById(R.id.btnCancelarReserva);

                                // Pega os dados do documento
                                String docId = document.getId(); // O ID do documento para apagar
                                String labNome = document.getString("nome_laboratorio");
                                String horario = document.getString("horario_bloco");
                                String dataStr = document.getString("data_reserva_str");
                                String descricao = document.getString("descricao");

                                // Preenche os TextViews
                                tvNome.setText(String.format("%s (%s)", labNome, horario));
                                tvData.setText("Dia: " + dataStr);
                                tvDescricao.setText("Motivo: " + descricao);

                                // --- FUNCIONALIDADE DE CANCELAR (ADGLI-103) ---
                                btnCancelar.setOnClickListener(v -> {
                                    // Pergunta ao usuário se ele tem certeza
                                    new AlertDialog.Builder(this)
                                            .setTitle("Cancelar Reserva")
                                            .setMessage("Tem certeza que deseja cancelar esta reserva?")
                                            .setPositiveButton("Sim, Cancelar", (dialog, which) -> {
                                                cancelarReserva(docId);
                                            })
                                            .setNegativeButton("Não", null)
                                            .show();
                                });

                                // Adiciona o card pronto à tela
                                layoutReservas.addView(reservaCard);
                            }
                        }
                    } else {
                        Log.w(TAG, "Erro ao buscar reservas.", task.getException());
                        Toast.makeText(MainActivity.this, "Erro ao carregar reservas.", Toast.LENGTH_SHORT).show();
                    }
                    showLoading(false);
                });
    }

    /**
     * Apaga um documento de reserva do Firestore (ADGLI-103)
     */
    private void cancelarReserva(String reservaId) {
        showLoading(true); // Mostra o progresso

        db.collection("reservas").document(reservaId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Reserva cancelada com sucesso!");
                    Toast.makeText(MainActivity.this, "Reserva cancelada.", Toast.LENGTH_SHORT).show();
                    // Recarrega a lista para mostrar a mudança
                    loadReservas();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erro ao cancelar reserva", e);
                    Toast.makeText(MainActivity.this, "Erro ao cancelar reserva.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }


    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBarMain.setVisibility(View.VISIBLE);
        } else {
            progressBarMain.setVisibility(View.GONE);
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
            mAuth.signOut();
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a MainActivity
    }
}