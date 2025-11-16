package com.example.app_reserva_laboratorio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.service.ReservaService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

    /**
     * Activity responsável por exibir as reservas do usuário e permitir a edição
     * de reservas de laboratórios e estações individuais.
     */
    public class MinhasReservasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
            ReservasAdapter.OnReservaClickListener{
        // Componentes do menu lateral
        private DrawerLayout drawerLayout;
        private ImageButton menuButton;
        private ImageButton backButton;

        // Componentes principais
        private RecyclerView recyclerView;
        private ReservaService reservaService;
        private ReservaRepository reservaRepository;
        private ReservasAdapter adapter;
        private List<Reserva> minhasReservas;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        //Inicializa o Back-end
        reservaService = new ReservaService();
        reservaRepository = ReservaRepository.getInstance();

        // Configuração do Navigation Drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_reservas);

        // Lógica para os botões do cabeçalho
        menuButton = findViewById(R.id.menu_button);
        backButton = findViewById(R.id.back_button);

        // Abre o menu lateral ao clicar no botão de menu
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Fecha a Activity ao clicar no botão voltar
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Lógica do RecyclerView

        // Encontra o RecyclerView no XML
        recyclerView = findViewById(R.id.recycler_view_reservas);

        // Define o Gerenciador de Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Busca os dados reais do "banco de dados"
        String alunoId = "aluno_teste_123";
        minhasReservas = reservaService.getMinhasReservas(alunoId);

        // Cria e conecta o Adapter
        adapter = new ReservasAdapter(minhasReservas, this, reservaRepository);
        recyclerView.setAdapter(adapter);

        // Configuração do botão Voltar
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

        /**
         * Método onResume() é chamado toda vez que a tela volta ao foco.
         * (Quando você cria, e quando você volta da tela de Edição/Cancelamento).
         */
        @Override
        protected void onResume() {
            super.onResume();

            // Se o adapter já existe nós atualizamos a lista.
            if (adapter != null) {
                // Busca a lista atualizada do "banco de dados"
                String alunoId = "aluno_teste_123";
                List<Reserva> listaAtualizada = reservaService.getMinhasReservas(alunoId);

                // Limpa a lista antiga (que o adapter está usando)
                minhasReservas.clear();
                // Adiciona os novos dados na lista
                minhasReservas.addAll(listaAtualizada);

                // Avisa o adapter que os dados mudaram, removendo o card
                adapter.notifyDataSetChanged();
            }
        }

    /**
     * É chamado pelo Adapter quando o usuário clica no botão de editar.
     * Decide para qual tela de edição o app deve ir.
     */
        @Override
    public void onEditClick(Reserva reserva) {
        String idReserva = reserva.getIdReserva();
        String labId = reserva.getLaboratorioId();

        Intent intent;

        if (labId != null && labId.startsWith("est_")) {
            // Se for uma estação, abre a tela de Editar Estação
            intent = new Intent(this, EditarEstacaoActivity.class);
            intent.putExtra("ID_RESERVA", idReserva);
        } else {
            // Se for um laboratório
            intent = new Intent(this, EditarReservaActivity.class);
            intent.putExtra("ID_RESERVA", idReserva);
        }
        startActivity(intent); // Inicia a Activity correta
    }

    // Ações ao clicar em um item do menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Toast.makeText(this, "Clicou em Perfil", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_reservas) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_sair) {
            Toast.makeText(this, "Saindo", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
