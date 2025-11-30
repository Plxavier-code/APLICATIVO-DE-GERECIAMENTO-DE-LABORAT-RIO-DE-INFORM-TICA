package com.example.a2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2.R;
import com.example.a2.data.Laboratorio;
import com.example.a2.service.ReservaService;

import java.util.List;
import java.util.stream.Collectors;

public class ListaLaboratoriosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_laboratorios);

        Toolbar toolbar = findViewById(R.id.toolbarLista);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Escolha um Laboratório");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Botão de voltar

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLabs);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Exibe em 2 colunas

        ReservaService reservaService = new ReservaService();
        // Filtra para mostrar apenas laboratórios inteiros, não estações
        List<Laboratorio> laboratorios = reservaService.getLaboratorios().stream()
                .filter(l -> !l.getNome().contains("Estação"))
                .collect(Collectors.toList());

        LabsAdapter adapter = new LabsAdapter(laboratorios, lab -> {
            Intent intent = new Intent(ListaLaboratoriosActivity.this, AgendamentoActivity.class);
            intent.putExtra("LAB_ID", lab.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Ação do botão de voltar
        return true;
    }
}
