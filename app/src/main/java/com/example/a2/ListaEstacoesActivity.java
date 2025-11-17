package com.example.a2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ListaEstacoesActivity extends AppCompatActivity { // 1. Nome da Classe

    private static final String TAG = "ListaEstacoes"; // 2. Tag

    private FirebaseFirestore db;
    private Toolbar toolbarLista;
    private LinearLayout layoutLaboratorios; // O ID pode ser o mesmo do XML
    private ProgressBar progressBarLista;
    private TextView tvNenhumLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 3. Carrega o layout de estações
        setContentView(R.layout.activity_lista_estacoes);

        db = FirebaseFirestore.getInstance();

        // 4. A toolbar pode ter o mesmo ID, mas vamos mudar o título
        toolbarLista = findViewById(R.id.toolbarLista);
        toolbarLista.setTitle("Estações Disponíveis"); // Título novo
        setSupportActionBar(toolbarLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutLaboratorios = findViewById(R.id.layoutLaboratorios);
        progressBarLista = findViewById(R.id.progressBarLista);
        tvNenhumLab = findViewById(R.id.tvNenhumLab);
        tvNenhumLab.setText("Nenhuma estação encontrada."); // Texto novo

        loadEstacoes(); // 5. Chama o método de carregar estações
    }

    private void loadEstacoes() { // 6. Nome do método
        showLoading(true);
        layoutLaboratorios.removeAllViews();
        tvNenhumLab.setVisibility(View.GONE);

        // 7. Busca na coleção "Estacoes" (com 'E' maiúsculo)
        db.collection("Estacoes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            tvNenhumLab.setVisibility(View.VISIBLE);
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addEstacaoCard(document); // 8. Chama o card de estação
                            }
                        }
                    } else {
                        Log.w(TAG, "Erro ao buscar estações.", task.getException());
                        Toast.makeText(this, "Erro ao carregar estações.", Toast.LENGTH_SHORT).show();
                    }
                    showLoading(false);
                });
    }

    private void addEstacaoCard(QueryDocumentSnapshot document) { // 9. Nome do método
        String estacaoId = document.getId(); // 10. Variável
        String nome = document.getString("nome");
        String capacidade = document.getString("capacidade");
        String descricao = document.getString("descricao");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 11. Infla o layout do item de estação
        View labCardView = inflater.inflate(R.layout.list_item_estacao, layoutLaboratorios, false);

        TextView tvLabNome = labCardView.findViewById(R.id.tvLabNome);
        TextView tvLabCapacidade = labCardView.findViewById(R.id.tvLabCapacidade);
        TextView tvLabDescricao = labCardView.findViewById(R.id.tvLabDescricao);

        tvLabNome.setText(nome);
        tvLabCapacidade.setText("Capacidade: " + capacidade);
        tvLabDescricao.setText(descricao);

        labCardView.setOnClickListener(v -> {
            // 12. Abre a tela de Agendamento de Estação
            Intent intent = new Intent(ListaEstacoesActivity.this, AgendamentoEstacaoActivity.class);
            // 13. Envia os IDs corretos
            intent.putExtra("ESTACAO_ID", estacaoId);
            intent.putExtra("ESTACAO_NOME", nome);
            startActivity(intent);
        });

        layoutLaboratorios.addView(labCardView);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBarLista.setVisibility(View.VISIBLE);
        } else {
            progressBarLista.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}