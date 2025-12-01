package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.ReservaRepository;

public class ConfirmacaoManutencaoActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private ImageView imageViewItem;
    private TextView textViewNomeItem;
    private TextView textViewCapacidade;
    private TextView textViewLocalizacao;
    private Spinner spinnerTipoManutencao;
    private EditText editTextObservacoes;
    private Button btnConfirmarManutencao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_manutencao);

        imageViewItem = findViewById(R.id.imageViewItem);
        textViewNomeItem = findViewById(R.id.textViewNomeItem);
        textViewCapacidade = findViewById(R.id.textViewCapacidade);
        textViewLocalizacao = findViewById(R.id.textViewLocalizacao);
        spinnerTipoManutencao = findViewById(R.id.spinnerTipoManutencao);
        editTextObservacoes = findViewById(R.id.editTextObservacoes);
        btnConfirmarManutencao = findViewById(R.id.btnConfirmarManutencao);

        String itemId = getIntent().getStringExtra(EXTRA_ITEM_ID);
        Laboratorio item = ReservaRepository.getInstance().getLaboratorioById(itemId);

        if (item != null) {
            textViewNomeItem.setText(item.getNome());
            textViewCapacidade.setText("Capacidade: " + item.getCapacidade() + " computadores");
            textViewLocalizacao.setText(item.getLocalizacao());

            // Carregar imagem (a ser implementado)
            // imageViewItem.setImageResource(...);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Preventiva", "Corretiva"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoManutencao.setAdapter(adapter);

        btnConfirmarManutencao.setOnClickListener(v -> {
            // Lógica para salvar a manutenção
            Toast.makeText(this, "Manutenção confirmada!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
