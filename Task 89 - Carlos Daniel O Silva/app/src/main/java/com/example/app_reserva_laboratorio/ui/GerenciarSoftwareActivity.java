package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Software;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GerenciarSoftwareActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSoftware;
    private SoftwareAdapter softwareAdapter;
    private List<Software> softwareList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_software);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewSoftware = findViewById(R.id.recycler_view_software);
        recyclerViewSoftware.setLayoutManager(new LinearLayoutManager(this));

        // Lista de exemplos de software
        softwareList = new ArrayList<>();
        softwareList.add(new Software("Android Studio", "Hedgehog | 2023.1.1"));
        softwareList.add(new Software("Visual Studio Code", "1.85"));
        softwareList.add(new Software("Figma", "Versão atual da Web"));

        softwareAdapter = new SoftwareAdapter(softwareList);
        recyclerViewSoftware.setAdapter(softwareAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_software);
        fab.setOnClickListener(view -> showAddSoftwareDialog());
    }

    private void showAddSoftwareDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Novo Software");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        final EditText inputNome = new EditText(this);
        inputNome.setHint("Nome do Software");
        layout.addView(inputNome);

        final EditText inputVersao = new EditText(this);
        inputVersao.setHint("Versão");
        layout.addView(inputVersao);

        builder.setView(layout);

        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            String nome = inputNome.getText().toString();
            String versao = inputVersao.getText().toString();
            if (!nome.isEmpty() && !versao.isEmpty()) {
                softwareList.add(new Software(nome, versao));
                softwareAdapter.notifyItemInserted(softwareList.size() - 1);
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
