package ar.edu.uade.desa1.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

import ar.edu.uade.desa1.R;
import ar.edu.uade.desa1.RoutesActivity;
import ar.edu.uade.desa1.api.RoutesApiService;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponseWithUserInfo;
import ar.edu.uade.desa1.util.RoleEnum;
import ar.edu.uade.desa1.util.TokenManager;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RouteCardFragment extends Fragment {

    public static final String AVAILABLE = "AVAILABLE";
    public static final String IN_PROGRESS = "IN_PROGRESS";

    @Inject
    TokenManager tokenManager;

    @Inject
    RoutesApiService routesApiService;

    private DeliveryRouteResponseWithUserInfo route;

    public static RouteCardFragment newInstance(DeliveryRouteResponseWithUserInfo route) {
        RouteCardFragment fragment = new RouteCardFragment();
        Bundle args = new Bundle();
        args.putSerializable("route", route);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_card, container, false);

        TextView titleText = view.findViewById(R.id.titleText);
        TextView subtitleText = view.findViewById(R.id.subtitleText);
        TextView statusText = view.findViewById(R.id.statusText);
        TextView footerText = view.findViewById(R.id.footerText);
        Button actionButton = view.findViewById(R.id.actionButton);

        if (getArguments() != null) {
            String role = tokenManager.getUserRoleFromToken();
            route = (DeliveryRouteResponseWithUserInfo) getArguments().getSerializable("route");

            titleText.setText("Desde: " + route.getOrigin());
            subtitleText.setText("Hacia: " + route.getDestination());

            String status = route.getStatus();
            String statusSpanish = getSpanishStatus(status);
            statusText.setText("Estado: " + statusSpanish);

            boolean isRepartidor = RoleEnum.REPARTIDOR == RoleEnum.valueOf(role);

            String formattedDate = formatDate(route.getUpdatedAt());
            footerText.setText(isRepartidor ? "Cliente: " + route.getUserInfo() + "\nFecha: " + formattedDate : "Fecha: " + formattedDate);


            if (isRepartidor) {
                if (AVAILABLE.equalsIgnoreCase(status)) {
                    actionButton.setText("Asignarme ruta");
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.holo_green_light));
                    actionButton.setTextColor(Color.WHITE);
                    actionButton.setOnClickListener(v -> updateRouteStatus("IN_PROGRESS"));
                } else if (IN_PROGRESS.equalsIgnoreCase(status)) {
                    actionButton.setText("Finalizar ruta");
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.holo_red_light));
                    actionButton.setTextColor(Color.WHITE);
                    actionButton.setOnClickListener(v -> updateRouteStatus("COMPLETED"));
                }
            }
        }

        return view;
    }

    private void updateRouteStatus(String newStatus) {
        Long userId = tokenManager.getUserIdFromToken();
        UpdateRouteStatusRequest request = new UpdateRouteStatusRequest(route.getId(), newStatus, userId);

        routesApiService.updateRouteStatus(request).enqueue(new Callback<DeliveryRouteResponseWithUserInfo>() {
            @Override
            public void onResponse(Call<DeliveryRouteResponseWithUserInfo> call, Response<DeliveryRouteResponseWithUserInfo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Estado actualizado a " + getSpanishStatus(newStatus), Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof RoutesActivity) {
                        ((RoutesActivity) getActivity()).reloadRoutes();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al actualizar ruta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryRouteResponseWithUserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSpanishStatus(String status) {
        try {
            return RouteStatusEnum.valueOf(status).getSpanishStatus();
        } catch (Exception e) {
            return status;
        }
    }

    private String formatDate(String fullDateTime) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(fullDateTime);
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return fullDateTime;
        }
    }
}
