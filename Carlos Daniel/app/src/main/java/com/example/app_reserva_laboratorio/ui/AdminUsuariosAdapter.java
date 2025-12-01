package com.example.app_reserva_laboratorio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Usuario;
import java.util.List;

public class AdminUsuariosAdapter extends RecyclerView.Adapter<AdminUsuariosAdapter.UsuarioAdminViewHolder> {

    private List<Usuario> listaUsuarios;
    private OnUsuarioAdminClickListener clickListener;

    public AdminUsuariosAdapter(List<Usuario> listaUsuarios, OnUsuarioAdminClickListener clickListener, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.clickListener = clickListener;
    }

    public interface OnUsuarioAdminClickListener {
        void onRemoverClick(Usuario usuario);
        void onEditarClick(Usuario usuario);
    }

    @NonNull
    @Override
    public UsuarioAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usuario_admin_item, parent, false);
        return new UsuarioAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdminViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        holder.textNome.setText(usuario.getNome());
        holder.textEmail.setText(usuario.getEmail());

        if ("Aluno".equals(usuario.getTipo())) {
            holder.textTipo.setText("Aluno - Matrícula: " + usuario.getIdentificador());
        } else {
            holder.textTipo.setText("Professor - Depto: " + usuario.getIdentificador());
        }

        holder.btnEditar.setOnClickListener(v -> clickListener.onEditarClick(usuario));
        holder.btnRemover.setOnClickListener(v -> clickListener.onRemoverClick(usuario));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioAdminViewHolder extends RecyclerView.ViewHolder {
        TextView textNome;
        TextView textEmail;
        TextView textTipo;
        ImageButton btnEditar;
        ImageButton btnRemover;

        public UsuarioAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textNome = itemView.findViewById(R.id.text_admin_usuario_nome);
            textEmail = itemView.findViewById(R.id.text_admin_usuario_email);
            textTipo = itemView.findViewById(R.id.text_admin_usuario_tipo);
            btnEditar = itemView.findViewById(R.id.btn_admin_usuario_editar);
            btnRemover = itemView.findViewById(R.id.btn_admin_usuario_remover);
        }
    }
}
