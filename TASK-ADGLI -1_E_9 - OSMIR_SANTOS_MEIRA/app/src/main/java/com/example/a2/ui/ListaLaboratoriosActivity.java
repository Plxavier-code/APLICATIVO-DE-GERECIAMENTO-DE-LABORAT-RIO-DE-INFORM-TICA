package com.example.a2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.example.a2.data.Laboratorio;
import com.example.a2.data.ReservaRepository;

import java.util.List;

public class ListaLaboratoriosActivity extends AppCompatActivity {

    private LinearLayout layoutLaboratorios;
    private ProgressBar progressBarLista;
    private TextView tvNenhumLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_laboratorios);

        Toolbar toolbar = findViewById(R.id.toolbarLista);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutLaboratorios = findViewById(R.id.layoutLaboratorios);
        progressBarLista = findViewById(R.id.progressBarLista);
        tvNenhumLab = findViewById(R.id.tvNenhumLab);

        carregarLaboratorios();
    }

    private void carregarLaboratorios() {
        // Simulação de carregamento (não é necessário delay real em memória)
        progressBarLista.setVisibility(View.GONE);
        layoutLaboratorios.removeAllViews();

        List<Laboratorio> labs = ReservaRepository.getInstance().getLaboratorios();

        if (labs.isEmpty()) {
            tvNenhumLab.setVisibility(View.VISIBLE);
        } else {
            tvNenhumLab.setVisibility(View.GONE);
            for (Laboratorio lab : labs) {
                addLaboratorioCard(lab);
            }
        }
    }

    private void addLaboratorioCard(Laboratorio lab) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.list_item_laboratorio, layoutLaboratorios, false);

        TextView tvNome = card.findViewById(R.id.tvLabNome);
        TextView tvCap = card.findViewById(R.id.tvLabCapacidade);
        TextView tvDesc = card.findViewById(R.id.tvLabDescricao);

        tvNome.setText(lab.getNome());
        tvCap.setText("Capacidade: " + lab.getCapacidade());
        tvDesc.setText(lab.getLocalizacao()); // Usando localização como descrição

        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendamentoActivity.class);
            intent.putExtra("LABORATORIO_ID", lab.getId());
            intent.putExtra("LABORATORIO_NOME", lab.getNome());
            startActivity(intent);
        });

        layoutLaboratorios.addView(card);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}