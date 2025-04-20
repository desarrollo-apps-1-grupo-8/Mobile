package ar.edu.uade.desa1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ar.edu.uade.desa1.R;
import ar.edu.uade.desa1.domain.RouteStatusEnum;
import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;

public class HistoryRouteCardFragment extends Fragment {

    private static final String ARG_PACKAGE = "packageInfo";
    private static final String ARG_ORIGIN = "origin";
    private static final String ARG_DESTINATION = "destination";
    private static final String ARG_STATUS = "status";

    public static HistoryRouteCardFragment newInstance(DeliveryRouteResponse route) {
        HistoryRouteCardFragment fragment = new HistoryRouteCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PACKAGE, route.getPackageInfo());
        args.putString(ARG_ORIGIN, route.getOrigin());
        args.putString(ARG_DESTINATION, route.getDestination());
        args.putString(ARG_STATUS, route.getStatus());
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

        ((TextView) view.findViewById(R.id.titleText)).setText(packageInfo);
        ((TextView) view.findViewById(R.id.subtitleText)).setText(origin + " ➝ " + destination);
        ((TextView) view.findViewById(R.id.statusText)).setText(
                RouteStatusEnum.valueOf(status).getSpanishStatus()
        );
        ((TextView) view.findViewById(R.id.footerText)).setText("Cliente: (sin datos)   Fecha: (sin datos)");

        return view;
    }
}
