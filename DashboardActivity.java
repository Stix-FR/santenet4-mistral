
package com.example.healthtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configuration de la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configuration du drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configuration navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Afficher le nom d'utilisateur
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);
        
        SharedPreferences sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Utilisateur");
        textViewUsername.setText(username);

        // Configuration du ViewPager
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // Configuration TabLayout
        tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Tension");
                            break;
                        case 1:
                            tab.setText("Diabète");
                            break;
                        case 2:
                            tab.setText("IMC");
                            break;
                        case 3:
                            tab.setText("Souffle");
                            break;
                    }
                }).attach();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_export) {
            // Exporter les données de santé
            exportHealthData();
        } else if (id == R.id.nav_settings) {
            // Ouvrir les paramètres
            // TODO: Implémenter
        } else if (id == R.id.nav_logout) {
            // Déconnexion
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exportHealthData() {
        // Export des données en local
        Utils.exportDataToCSV(this);
    }

    private void logout() {
        // Effacer l'état de connexion
        SharedPreferences sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        
        // Retour à la page de connexion
        finish();
    }
}
