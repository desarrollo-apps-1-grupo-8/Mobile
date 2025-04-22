package ar.edu.uade.desa1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponseWithUserInfo;
import ar.edu.uade.desa1.fragment.RouteCardFragment;
import ar.edu.uade.desa1.util.AuthRouteHandler;
import ar.edu.uade.desa1.util.NetworkUtils;
import ar.edu.uade.desa1.util.RoleEnum;
import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RoutesActivity extends AppCompatActivity {

    private LinearLayout routesContainer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noRoutesText;

    @Inject
    RoutesApiService routesApiService;

    @Inject
    AuthRouteHandler authRouteHandler;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!NetworkUtils.isConnected(this)) {
            startActivity(new Intent(this, NoInternetActivity.class));
            finish();
            return;
        }

        if (!authRouteHandler.checkAuthentication(this, LoginActivity.class)) { //
            return;
        }

        setContentView(R.layout.activity_routes);

        routesContainer = findViewById(R.id.routesContainer);
        noRoutesText = findViewById(R.id.noRoutesText);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        
        swipeRefreshLayout.setOnRefreshListener(this::loadRoutes);
        
        loadRoutes();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameText = headerView.findViewById(R.id.username_text);
        TextView roleText = headerView.findViewById(R.id.role_text);

        String firstName = tokenManager.getUserFirstNameFromToken();
        String role = tokenManager.getUserRoleFromToken();
        usernameText.setText("Hola " + firstName + "!");
        roleText.setText(role.toUpperCase());
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                tokenManager.clearToken();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }
    
    private void loadRoutes() {
        System.out.println("Role: " + tokenManager.getUserRoleFromToken());
        long userId = tokenManager.getUserIdFromToken();
        RoleEnum role = RoleEnum.valueOf(tokenManager.getUserRoleFromToken());
        
        // Clear existing routes
        routesContainer.removeAllViews();
        
        if (role == RoleEnum.USUARIO) {
            loadUserRoutes(userId);
        } else if (role == RoleEnum.REPARTIDOR) {
            loadAllRoutes();
        }
    }
    
    private void loadUserRoutes(long userId) {
        routesApiService.getRoutesByUserId(userId)
                .enqueue(new Callback<List<DeliveryRouteResponseWithUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryRouteResponseWithUserInfo>> call, Response<List<DeliveryRouteResponseWithUserInfo>> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<DeliveryRouteResponseWithUserInfo> rutas = response.body();

                            if (rutas.isEmpty()) {
                                noRoutesText.setVisibility(View.VISIBLE);
                            } else {
                                noRoutesText.setVisibility(View.GONE);
                                for (DeliveryRouteResponseWithUserInfo route : rutas) {
                                    addRouteCard(route);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryRouteResponseWithUserInfo>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("RoutesActivity", "Error en Retrofit", t);
                        Toast.makeText(RoutesActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void loadAllRoutes() {
        routesApiService.getAllRoutes()
                .enqueue(new Callback<List<DeliveryRouteResponseWithUserInfo>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryRouteResponseWithUserInfo>> call, Response<List<DeliveryRouteResponseWithUserInfo>> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<DeliveryRouteResponseWithUserInfo> rutas = response.body();

                            if (rutas.isEmpty()) {
                                noRoutesText.setVisibility(View.VISIBLE);
                            } else {
                                noRoutesText.setVisibility(View.GONE);
                                for (DeliveryRouteResponseWithUserInfo route : rutas) {
                                    addRouteCard(route);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryRouteResponseWithUserInfo>> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("RoutesActivity", "Error en Retrofit", t);
                        Toast.makeText(RoutesActivity.this, "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void addRouteCard(DeliveryRouteResponseWithUserInfo route) {
        Fragment fragment = RouteCardFragment.newInstance(route);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.routesContainer, fragment)
                .commit();
    }

    public void reloadRoutes() {
        loadRoutes();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { /// /burger options
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}