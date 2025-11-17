package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import java.util.List;

public class GerenciarLabsActivity extends AppCompatActivity implements AdminLabsAdapter.OnLabAdminClickListener {

    private AdminLabsAdapter adapter;
    private List<Laboratorio> listaDeLaboratorios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_labs);

        ReservaRepository reservaRepository = ReservaRepository.getInstance();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_admin_labs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaDeLaboratorios = reservaRepository.getLaboratorios();

        adapter = new AdminLabsAdapter(listaDeLaboratorios, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onManutencaoClick(Laboratorio lab) {
        lab.setEmManutencao(!lab.isEmManutencao());
        adapter.notifyDataSetChanged();
        String status = lab.isEmManutencao() ? "em manutenção" : "disponível";
        Toast.makeText(this, lab.getNome() + " agora está " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBloquearClick(Laboratorio lab) {
        lab.setDisponivel(!lab.isDisponivel());
        adapter.notifyDataSetChanged();
        String status = lab.isDisponivel() ? "Desbloqueado" : "Bloqueado";
        Toast.makeText(this, lab.getNome() + " foi " + status, Toast.LENGTH_SHORT).show();
    }
}
