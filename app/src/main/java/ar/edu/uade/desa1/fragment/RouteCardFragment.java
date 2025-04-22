package ar.edu.uade.desa1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ar.edu.uade.desa1.R;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponseWithUserInfo;

public class RouteCardFragment extends Fragment {

    private static final String ARG_PACKAGE = "packageInfo";
    private static final String ARG_ORIGIN = "origin";
    private static final String ARG_DESTINATION = "destination";
    private static final String ARG_STATUS = "status";
    private static final String ARG_CREATED_AT_DATE = "createdAt";
    private static final String ARG_UPDATED_AT_DATE = "updatedAt";
    public static final String ARG_CLIENT = "client";


    public static RouteCardFragment newInstance(DeliveryRouteResponseWithUserInfo route) {
        RouteCardFragment fragment = new RouteCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE, route.getPackageInfo());
        args.putString(ARG_ORIGIN, route.getOrigin());
        args.putString(ARG_DESTINATION, route.getDestination());
        args.putString(ARG_STATUS, route.getStatus());
        args.putString(ARG_CREATED_AT_DATE, route.getCreatedAt().toString());
        args.putString(ARG_UPDATED_AT_DATE, route.getUpdatedAt().toString());
        args.putString(ARG_CLIENT, route.getUserInfo());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_route_card, container, false);

        String packageInfo = getArguments().getString(ARG_PACKAGE);
        String origin = getArguments().getString(ARG_ORIGIN);
        String destination = getArguments().getString(ARG_DESTINATION);
        String status = getArguments().getString(ARG_STATUS);
        String createdAt = getArguments().getString(ARG_CREATED_AT_DATE);
        String updatedAt = getArguments().getString(ARG_UPDATED_AT_DATE);
        String client = getArguments().getString(ARG_CLIENT);

        String onlyDate = updatedAt.split("T")[0];

        ((TextView) view.findViewById(R.id.titleText)).setText(packageInfo);
        ((TextView) view.findViewById(R.id.subtitleText)).setText(origin + " ➝ " + destination);
        ((TextView) view.findViewById(R.id.statusText)).setText(
                RouteStatusEnum.valueOf(status).getSpanishStatus()
        );
        ((TextView) view.findViewById(R.id.footerText)).setText("Cliente: " + client + "\nFecha: " + onlyDate);

        return view;
    }

}
