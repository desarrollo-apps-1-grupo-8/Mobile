package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;

/// // bur
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
/// // ger

/**
 * Actividad principal - HOME
 */
public class HomeActivity extends BaseActivity {

    /// / bur
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //// ger

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///////// bur
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Mostrar el ícono de hamburguesa
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Nombre de usuario en el header
        View headerView = navigationView.getHeaderView(0);
        TextView usernameText = headerView.findViewById(R.id.username_text);
        usernameText.setText("Hola, Juan!");

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish(); // cerrar la sesión y evitar volver con "Back"
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        /// // ger
 }

    /// bur
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { /// /burger options
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /// /ger
} 