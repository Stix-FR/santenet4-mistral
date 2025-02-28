
package com.example.healthtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        
        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);

        // Vérifier si l'utilisateur est déjà connecté
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startDashboardActivity();
            return;
        }

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            // Authentification locale simple
            if (validateCredentials(username, password)) {
                // Sauvegarder l'état de connexion
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", username);
                editor.apply();
                
                startDashboardActivity();
            } else {
                Toast.makeText(MainActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        // Pour la démo, on accepte un utilisateur par défaut
        // Dans une vraie application, ces infos seraient stockées de manière sécurisée
        return username.equals("user") && password.equals("password");
    }

    private void startDashboardActivity() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
