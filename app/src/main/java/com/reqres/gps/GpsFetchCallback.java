package com.reqres.gps;

public interface GpsFetchCallback {

    void onLocationUpdate(double latitude, double longitude);
}