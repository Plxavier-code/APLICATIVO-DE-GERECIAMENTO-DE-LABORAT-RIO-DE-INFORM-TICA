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

public class ListaLaboratoriosActivity extends AppCompatActivity {

    private static final String TAG = "ListaLaboratorios";

    private FirebaseFirestore db;
    private Toolbar toolbarLista;
    private LinearLayout layoutLaboratorios;
    private ProgressBar progressBarLista;
    private TextView tvNenhumLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_laboratorios);

        // Inicializa o Firestore
        db = FirebaseFirestore.getInstance();

        // Configura a Toolbar
        toolbarLista = findViewById(R.id.toolbarLista);
        setSupportActionBar(toolbarLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Vincula os componentes
        layoutLaboratorios = findViewById(R.id.layoutLaboratorios);
        progressBarLista = findViewById(R.id.progressBarLista);
        tvNenhumLab = findViewById(R.id.tvNenhumLab);

        // Carrega os dados
        loadLaboratorios();
    }

    private void loadLaboratorios() {
        showLoading(true);
        layoutLaboratorios.removeAllViews(); // Limpa a lista
        tvNenhumLab.setVisibility(View.GONE);

        // Busca a coleção "laboratorios" no Firestore
        db.collection("Laboratorios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            tvNenhumLab.setVisibility(View.VISIBLE);
                        } else {
                            // Para cada documento (laboratório) encontrado...
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // ...cria um cartão e adiciona na tela
                                addLaboratorioCard(document);
                            }
                        }
                    } else {
                        Log.w(TAG, "Erro ao buscar laboratórios.", task.getException());
                        Toast.makeText(this, "Erro ao carregar laboratórios.", Toast.LENGTH_SHORT).show();
                    }
                    showLoading(false);
                });
    }

    /**
     * Cria (infla) um "list_item_laboratorio.xml", preenche com os dados
     * e o adiciona no "layoutLaboratorios".
     */
    private void addLaboratorioCard(QueryDocumentSnapshot document) {
        // Pega os dados do documento
        String labId = document.getId();
        String nome = document.getString("nome");
        String capacidade = document.getString("capacidade");
        String descricao = document.getString("descricao");

        // "Infla" (cria) o layout do item
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View labCardView = inflater.inflate(R.layout.list_item_laboratorio, layoutLaboratorios, false);

        // Vincula os componentes DENTRO do cartão
        TextView tvLabNome = labCardView.findViewById(R.id.tvLabNome);
        TextView tvLabCapacidade = labCardView.findViewById(R.id.tvLabCapacidade);
        TextView tvLabDescricao = labCardView.findViewById(R.id.tvLabDescricao);

        // Preenche os dados
        tvLabNome.setText(nome);
        tvLabCapacidade.setText("Capacidade: " + capacidade);
        tvLabDescricao.setText(descricao);

        // --- ATUALIZADO AQUI ---
        // Define o clique no cartão
        labCardView.setOnClickListener(v -> {
            // Abre a "Tela de Agendamento" (Figura 8)
            Intent intent = new Intent(ListaLaboratoriosActivity.this, AgendamentoActivity.class);
            // Envia os dados do laboratório selecionado para a próxima tela
            intent.putExtra("LABORATORIO_ID", labId);
            intent.putExtra("LABORATORIO_NOME", nome);
            startActivity(intent);
        });

        // Adiciona o cartão pronto à tela
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