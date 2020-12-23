package com.dylansalim.qrmenuapp.ui.merchant_info.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.AboutListItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.fragment.app.Fragment;

public class AboutMerchantFragment extends Fragment implements AboutFragmentViewInterface, OnMapReadyCallback {

    private MapView mapView;
    private ListView listView;
    private AboutFragmentPresenterInterface aboutFragmentPresenterInterface;
    private static final String TAG = "amf";

    public AboutMerchantFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMVP();
        Bundle bundle = getArguments();
        if (null != bundle && null != bundle.getParcelable(getResources().getString(R.string.store_result))) {
            Log.d(TAG, bundle.getParcelable(getResources().getString(R.string.store_result)).toString());
            aboutFragmentPresenterInterface.setStoreDetail(bundle.getParcelable(getResources().getString(R.string.store_result)));
        }
    }

    private void setupMVP() {
        this.aboutFragmentPresenterInterface = new AboutFragmentPresenter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_merchant, container, false);
        listView = (ListView) view.findViewById(R.id.about_merchant_list_view);
        aboutFragmentPresenterInterface.initStoreDetailView(getActivity());

        mapView = (MapView) view.findViewById(R.id.about_merchant_map_view);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Log.d(TAG, String.valueOf(i));
            if (i == 0) {
                // Address clicked
                Uri uri = aboutFragmentPresenterInterface.retrieveLocationUri();
                Log.d(TAG, uri.toString());
                if (null != uri) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        aboutFragmentPresenterInterface.onMapReady(googleMap, getContext());
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void setupListView(AboutListItem[] aboutListItems) {
        AboutArrayAdapter aboutArrayAdapter = new AboutArrayAdapter(getActivity(), aboutListItems);
        listView.setAdapter(aboutArrayAdapter);
    }
}