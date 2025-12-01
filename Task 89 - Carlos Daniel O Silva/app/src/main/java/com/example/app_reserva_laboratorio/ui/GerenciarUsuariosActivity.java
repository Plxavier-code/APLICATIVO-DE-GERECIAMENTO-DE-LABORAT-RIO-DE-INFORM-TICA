package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Usuario;
import java.util.List;

public class GerenciarUsuariosActivity extends AppCompatActivity implements AdminUsuariosAdapter.OnUsuarioAdminClickListener {

    private AdminUsuariosAdapter adapter;
    private List<Usuario> listaDeUsuarios;
    private ReservaRepository reservaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_usuarios);

        reservaRepository = ReservaRepository.getInstance();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_admin_usuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaDeUsuarios = reservaRepository.getUsuarios();
        adapter = new AdminUsuariosAdapter(listaDeUsuarios, this, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRemoverClick(Usuario usuario) {
        reservaRepository.removerUsuario(usuario.getId());
        listaDeUsuarios.remove(usuario);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Usuário " + usuario.getNome() + " removido.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditarClick(Usuario usuario) {
        Intent intent = new Intent(this, EditarUsuarioActivity.class);
        intent.putExtra("USER_ID", usuario.getId());
        startActivity(intent);
    }
}
