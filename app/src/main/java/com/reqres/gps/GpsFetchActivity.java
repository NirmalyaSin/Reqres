package com.reqres.gps;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class GpsFetchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GpsFetchActivity";
    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 0;
    public double lastLat = 0.0;
    public double lastLong = 0.0;
    //
    private final static int REQUEST_CHECK_SETTINGS = 1001;
    private final static int REQUEST_CHECK_LOCATION = 1002;
    FusedLocationProviderClient mFusedLocationClient;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    //
    private boolean isLocationNeeded = false;
    private GpsFetchCallback locationCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*initGps(isLocationNeeded, this);*/
    }

    public void initGps(final boolean isLocationNeeded, GpsFetchCallback locationCallback) {
        this.isLocationNeeded = isLocationNeeded;
        this.locationCallback = locationCallback;

        if (isLocationNeeded) {
            //Remove if any ongoing location update in progress.
            if (mLocationCallback != null && mFusedLocationClient != null) {
                try {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (GpsFetchActivity.this.locationCallback != null)
                            GpsFetchActivity.this.locationCallback.onLocationUpdate(location.getLatitude(),
                                    location.getLongitude());
                        lastLat = location.getLatitude();
                        lastLong = location.getLongitude();
                        Log.e("Location", "::::::: " + location.getLatitude() + " " + location.getLongitude());
                        stopLocationUpdates(isLocationNeeded);
                    }
                }
            };

            startLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setNumUpdates(1);

    }

    private void displayLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    Log.d("Location/", "All location settings are satisfied. The client can initialize location requests here.");

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            CommonMethods.makeLog(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                Log.d("Location/", "Cast to a resolvable exception.");

                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(GpsFetchActivity.this, REQUEST_CHECK_SETTINGS);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                                Log.d("Location/", "Ignore the error.");

                                // Ignore the error.
                            } catch (ClassCastException e) {
                                Log.d("Location/", "Ignore, should be an impossible error.");

                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
//                            CommonMethods.makeLog(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            Log.d("Location/", "Finish");

                            finish();
                            break;
                    }
                }
            }
        });
    }

    private void startLocationUpdates() {
        //check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_LOCATION);
            return;
        }
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            createLocationRequest();
            displayLocationSettingsRequest();
        } else {
            buildGoogleApiClient();
        }
    }

    public void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void stopLocationUpdates(boolean isLocationNeeded) {
        this.isLocationNeeded = isLocationNeeded;

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mGoogleApiClient.disconnect();
            mFusedLocationClient=null;
            isLocationNeeded=false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        Log.d("Location/", "Connection success.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Location/", "Connection failed: " + connectionResult.toString());
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isLocationNeeded)
            stopLocationUpdates();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
       /* if (isLocationNeeded)
            startLocationUpdates();*/
    }

    /*public static boolean isGooglePlayServicesAvailable(Activity activity) {

        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, status, 0).show();
            return false;
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CHECK_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission granted, Click again", Toast.LENGTH_SHORT).show();
                startLocationUpdates();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.d("Location/", "User agreed to make required location settings changes.");
                        try {
                            if (mFusedLocationClient != null
                                    && mLocationRequest != null
                                    && mLocationCallback != null)
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                        mLocationCallback,
                                        null /* Looper */);
                        } catch (SecurityException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case RESULT_CANCELED:
                        Log.d("Location/", "User choose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }
}
