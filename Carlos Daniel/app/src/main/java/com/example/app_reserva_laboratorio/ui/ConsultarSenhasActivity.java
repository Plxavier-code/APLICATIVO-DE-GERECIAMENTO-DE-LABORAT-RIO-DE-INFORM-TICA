package com.example.app_reserva_laboratorio.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.app_reserva_laboratorio.R;

public class ConsultarSenhasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_senhas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewSenha = findViewById(R.id.text_view_senha);
        // A senha está fixa no código, conforme o requisito.
        textViewSenha.setText("admin1234");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Define a ação do botão "Voltar" na toolbar
        return true;
    }
}
