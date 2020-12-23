package com.dylansalim.qrmenuapp.ui.merchant_info.about;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.AboutListItem;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.services.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AboutFragmentPresenter implements AboutFragmentPresenterInterface {

    private AboutFragmentViewInterface afvi;
    private StoreDao storeDetail;
    private GoogleMap googleMap;
    private static final String TAG = "afp";


    public AboutFragmentPresenter(AboutFragmentViewInterface afvi) {
        this.afvi = afvi;
    }

    @Override
    public void setStoreDetail(StoreDao storeDetail) {
        this.storeDetail = storeDetail;
    }

    @Override
    public void onMapReady(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        googleMap.setMinZoomPreference(12);
        googleMap.setIndoorEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        GpsTracker gpsTracker = new GpsTracker(context);
        gpsTracker.checkNetworkPermission();
        Log.d(TAG, "on map");

        if (gpsTracker.canGetLocation() && null != storeDetail && 0.0 != storeDetail.getLatitude() && 0.0 != storeDetail.getLongitude()) {
            Log.d(TAG, "entered");

            googleMap.setMyLocationEnabled(true);
            LatLng latLng = new LatLng(storeDetail.getLatitude(), storeDetail.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            googleMap.addMarker(markerOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            googleMap.setOnMarkerClickListener(marker -> {
                marker.showInfoWindow();
                return true;
            });
        }
    }

    @Override
    public void initStoreDetailView(Activity activity) {
        if (null != storeDetail) {
            String address = storeDetail.getAddress();
            String openHour = storeDetail.getOpenHour();
            String closingHour = storeDetail.getClosingHour();
            String specialOpeningNote = storeDetail.getSpecialOpeningNote();

            AboutListItem[] aboutListItems = new AboutListItem[]{new AboutListItem(activity.getResources().getDrawable(R.drawable.ic_baseline_location_on_24, activity.getTheme()), address),
                    new AboutListItem(activity.getResources().getDrawable(R.drawable.ic_baseline_access_time_24, activity.getTheme()), "Opening times", new String[]{String.format("%s - %s",openHour,closingHour), specialOpeningNote})};
            afvi.setupListView(aboutListItems);
        }
    }

    @Override
    public Uri retrieveLocationUri() {
        Log.d(TAG, "STORE DETAIL");
        Log.d(TAG, storeDetail.toString());
        if (null != storeDetail && null != storeDetail.getLatitude() && null != storeDetail.getLongitude() && null != storeDetail.getAddress()) {
            StringBuilder uriStringBuilder = new StringBuilder("geo:");
            uriStringBuilder.append(storeDetail.getLatitude() + ",");
            uriStringBuilder.append(storeDetail.getLongitude());
            String mappedAddress = storeDetail.getAddress().replace(",", "+").replace(" ","");
            uriStringBuilder.append("?q=" + mappedAddress);
            Log.d(TAG, "query map string" + uriStringBuilder.toString());
            return Uri.parse(uriStringBuilder.toString());
        }
        return null;
    }
}
