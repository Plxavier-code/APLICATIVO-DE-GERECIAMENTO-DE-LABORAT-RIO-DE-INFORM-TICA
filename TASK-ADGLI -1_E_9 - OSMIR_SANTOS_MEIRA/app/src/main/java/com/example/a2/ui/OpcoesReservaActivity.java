package ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2.R;
import com.google.android.material.card.MaterialCardView;

public class OpcoesReservaActivity extends AppCompatActivity {

    private Toolbar toolbarOpcoes;
    private MaterialCardView cardLaboratorio;
    private MaterialCardView cardEstacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_reserva);

        // Configura a Toolbar
        toolbarOpcoes = findViewById(R.id.toolbarOpcoes);
        setSupportActionBar(toolbarOpcoes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Vincula os componentes
        cardLaboratorio = findViewById(R.id.cardLaboratorio);
        cardEstacao = findViewById(R.id.cardEstacao);

        // --- Configura os cliques ---

        // Clique para reservar Laboratório
        cardLaboratorio.setOnClickListener(v -> {
            // Abre a "Tela de Lista de Laboratórios"
            Intent intent = new Intent(OpcoesReservaActivity.this, ListaLaboratoriosActivity.class);
            startActivity(intent);
        });

        // Clique para reservar Estação
        cardEstacao.setOnClickListener(v -> {
            // Abre a "Tela de Lista de Estações"
            Intent intent = new Intent(OpcoesReservaActivity.this, ListaEstacoesActivity.class);
            startActivity(intent);
        });
    }

    // Faz o botão "voltar" da toolbar funcionar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}