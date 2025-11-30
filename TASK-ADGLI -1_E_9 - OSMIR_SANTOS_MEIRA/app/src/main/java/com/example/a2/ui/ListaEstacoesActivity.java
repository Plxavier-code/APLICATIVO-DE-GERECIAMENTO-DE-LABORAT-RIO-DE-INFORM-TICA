package com.example.a2.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2.R;
import com.example.a2.data.Laboratorio;
import com.example.a2.service.ReservaService;

import java.util.List;
import java.util.stream.Collectors;

public class ListaEstacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_estacoes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Escolha uma Estação");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEstacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ReservaService reservaService = new ReservaService();
        // Filtra para mostrar apenas ESTAÇÕES, não laboratórios inteiros
        List<Laboratorio> estacoes = reservaService.getLaboratorios().stream()
                .filter(l -> l.getNome().contains("Estação"))
                .collect(Collectors.toList());

        // Reutiliza o mesmo LabsAdapter, pois a estrutura é a mesma
        LabsAdapter adapter = new LabsAdapter(estacoes, estacao -> {
            Intent intent = new Intent(ListaEstacoesActivity.this, AgendamentoActivity.class);
            intent.putExtra("LAB_ID", estacao.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
