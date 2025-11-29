package ui; // <-- MUITO IMPORTANTE: Este é o seu pacote

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Declaração dos componentes da UI
    private LinearLayout loginForm, registerForm;
    private EditText etEmail, etPassword, etRegNome, etRegEmail, etRegPassword;
    private Button btnLogin, btnRegister;
    private TextView tvToggleForm, tvLoginTitle;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isLoginFormVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Inicializa o Firebase Auth e Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginForm = findViewById(R.id.loginForm);
        registerForm = findViewById(R.id.registerForm);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRegNome = findViewById(R.id.etRegNome);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvToggleForm = findViewById(R.id.tvToggleForm);
        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        progressBar = findViewById(R.id.progressBar);

        // --- Configura os Listeners de Clique ---

        // Listener para o botão de Login (ADGLI-2)
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha e-mail e senha.", Toast.LENGTH_SHORT).show();
                return;
            }
            showLoading(true);
            signInProfessor(email, password);
        });

        // Listener para o botão de Cadastro (ADGLI-2)
        btnRegister.setOnClickListener(v -> {
            String nome = etRegNome.getText().toString().trim();
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            showLoading(true);
            registerProfessor(nome, email, password);
        });

        // Listener para alternar entre os formulários
        tvToggleForm.setOnClickListener(v -> toggleForms());
    }

    /**
     * Alterna a visibilidade entre o formulário de login e o de cadastro.
     */
    private void toggleForms() {
        if (isLoginFormVisible) {
            // Mostra Cadastro, Esconde Login
            loginForm.setVisibility(View.GONE);
            tvLoginTitle.setVisibility(View.GONE); // Esconde "LOGIN"
            registerForm.setVisibility(View.VISIBLE);
            tvToggleForm.setText("Já possui conta? Faça login aqui!");
        } else {
            // Mostra Login, Esconde Cadastro
            registerForm.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
            tvLoginTitle.setVisibility(View.VISIBLE); // Mostra "LOGIN"
            tvToggleForm.setText("Não possui cadastro? Cadastre-se aqui!");
        }
        isLoginFormVisible = !isLoginFormVisible;
    }

    /**
     * Tenta registrar um novo professor usando Firebase Auth e salvar
     * os dados no Firestore.
     */
    private void registerProfessor(String nome, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Usuário criado com sucesso, agora salva no Firestore
                        saveProfessorDataToFirestore(user, nome, email);
                    } else {
                        // Falha ao criar usuário no Auth
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Falha no cadastro: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        showLoading(false);
                    }
                });
    }

    /**
     * Salva os dados do professor no Firestore após o registro no Auth.
     */
    private void saveProfessorDataToFirestore(FirebaseUser user, String nome, String email) {
        String userId = user.getUid();

        // Cria um Map com os dados (conforme database_schema_m1.md)
        Map<String, Object> professor = new HashMap<>();
        professor.put("professor_id", userId);
        professor.put("nome_completo", nome);
        professor.put("email", email);
        professor.put("data_criacao", new Date()); // Data atual

        // Salva na coleção "professores" com o ID do usuário
        // ATENÇÃO: Verifique as regras de segurança do seu Firestore
        db.collection("professores").document(userId)
                .set(professor)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Documento salvo no Firestore com sucesso!");
                    showLoading(false);
                    Toast.makeText(LoginActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    navigateToMainApp();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erro ao salvar documento no Firestore", e);
                    Toast.makeText(LoginActivity.this, "Erro ao salvar dados: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    showLoading(false);
                });
    }

    /**
     * Tenta autenticar um professor existente usando Firebase Auth.
     */
    private void signInProfessor(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login com sucesso
                        Log.d(TAG, "signInWithEmail:success");
                        showLoading(false);
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        navigateToMainApp();
                    } else {
                        // Falha no login
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Falha no login: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                });
    }

    /**
     * Navega para a tela principal da aplicação (ex: MainActivity).
     */
    private void navigateToMainApp() {
        // Crie a Intent para a sua atividade principal
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finaliza a LoginActivity para que o usuário não volte para ela
    }

    /**
     * Mostra/Esconde a ProgressBar e desabilita/habilita os botões.
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            btnRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnRegister.setEnabled(true);
        }
    }

    // Opcional: Verificar se o usuário já está logado ao iniciar
    @Override
    public void onStart() {
        super.onStart();
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // if(currentUser != null){
        //    navigateToMainApp();
        // }
    }
}