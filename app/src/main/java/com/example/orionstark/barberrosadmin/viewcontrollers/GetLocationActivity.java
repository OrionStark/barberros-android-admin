package com.example.orionstark.barberrosadmin.viewcontrollers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orionstark.barberrosadmin.R;
import com.example.orionstark.barberrosadmin.services.AdminServices;
import com.example.orionstark.barberrosadmin.utils.BarberPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    GoogleMap myMap;
    Button submit, cancel_place_location;
    TextView place_text;
    private final int REQUEST_PERMISION_CODE = 3422;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    LatLng location;

    private LocationManager lm;
    BottomSheetBehavior sheetBehavior;
    LinearLayout bottomLayout;
    PlaceAutocompleteFragment placeAutocompleteFragment;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        initView();
    }

    private void initView() {
        place_text = findViewById(R.id.place_text);
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("IDN")
                .build();
        placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                place_text.setText(place.getName());
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17);
                myMap.animateCamera(location);
            }

            @Override
            public void onError(Status status) {

            }
        });
        placeAutocompleteFragment.setHint("Destination");
        placeAutocompleteFragment.setFilter(filter);

        bottomLayout = findViewById(R.id.bottomsheet_map_container);
        sheetBehavior = BottomSheetBehavior.from(bottomLayout);
        place_text = findViewById(R.id.place_text);
        sheetBehavior.setHideable(false);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        this.lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_layer);
        mapFragment.getMapAsync(this);
        submit = findViewById(R.id.submit_location_btn);
        cancel_place_location = findViewById(R.id.cancel_place_btn);
        setRequest();

        cancel_place_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(location.getLatitude(), location.getLongitude())
        ).zoom(20).build();
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        this.lm.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(GetLocationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_PERMISION_CODE);
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(cameraPosition.target.latitude,
                            cameraPosition.target.longitude, 1);
                    if ( null != listAddress && listAddress.size() > 0 ) {
                        place_text.setText(listAddress.get(0).getAddressLine(0));
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        address = listAddress.get(0).getAddressLine(0);
                        location = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "We can't get the place. Please change the direction or check your internet connection.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        myMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( address.equals("") || address == null || location == null ) {
                    dialogNetral("Oops", "Location information not set yet. Please check it back");
                } else {
                    try {
                        final ProgressDialog progressDialog = new ProgressDialog(GetLocationActivity.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        AdminServices.addBarber(
                                BarberPreference.getInstance(GetLocationActivity.this).getBarberInfo().get(0),
                                BarberPreference.getInstance(GetLocationActivity.this).getBarberInfo().get(1),
                                BarberPreference.getInstance(GetLocationActivity.this).getBarberInfo().get(2),
                                BarberPreference.getInstance(GetLocationActivity.this).getBarberInfo().get(3),
                                address,
                                String.valueOf(location.latitude),
                                String.valueOf(location.longitude),
                                GetLocationActivity.this,
                                new AdminServices.BarberCallback() {
                                    @Override
                                    public void onSucceed(String message) {
                                        progressDialog.cancel();
                                        BarberPreference.getInstance(GetLocationActivity.this).clearPreference();
                                        finishDialog("Well done", message);
                                    }

                                    @Override
                                    public void onError(String message) {
                                        progressDialog.cancel();
                                        dialogNetral("Oops", message);
                                    }
                                }
                        );
                    } catch (JSONException e) {
                        if ( e.getMessage() != null ) {
                            dialogNetral("Oops", e.getMessage());
                        } else {
                            dialogNetral("Oops", "Unknown Error");
                        }
                    }
                }
            }
        });

        cancel_place_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRequest() {
        GoogleApiClient GOOGLE_API_CLIENT = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        GOOGLE_API_CLIENT.connect();
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> res = LocationServices.SettingsApi.checkLocationSettings(GOOGLE_API_CLIENT, builder.build());
        res.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (ActivityCompat.checkSelfPermission(GetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(GetLocationActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(GetLocationActivity.this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            }, REQUEST_PERMISION_CODE);

                        } else {

                            lm.requestLocationUpdates("gps", 0, 0,GetLocationActivity.this);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,GetLocationActivity.this);

                        }
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(GetLocationActivity.this, 0x3); /* Need an Hex value */
                        } catch( Exception e ) {
                            Log.d("Error : ", e.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x3:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (ActivityCompat.checkSelfPermission(GetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GetLocationActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(GetLocationActivity.this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            }, REQUEST_PERMISION_CODE);

                        } else {
                            lm.requestLocationUpdates("gps", 0, 0, GetLocationActivity.this);
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        break;
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        Place place = PlaceAutocomplete.getPlace(this, data);
                        place_text.setText(place.getAddress());
                        break;
                }
        }
    }

    private void dialogNetral(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void finishDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
