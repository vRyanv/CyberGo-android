package com.tech.cybercars.utils;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class AnimatorUtil {
    private MapboxMap mapbox_map;
    public AnimatorUtil(MapboxMap mapbox_map){
        this.mapbox_map = mapbox_map;
    }

    public Animator CreateLatLngAnimator(LatLng currentPosition, LatLng targetPosition, long duration) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), currentPosition, targetPosition);
        latLngAnimator.setDuration(duration);
        latLngAnimator.setInterpolator(new FastOutSlowInInterpolator());
        latLngAnimator.addUpdateListener(animation -> mapbox_map.moveCamera(CameraUpdateFactory.newLatLng((LatLng) animation.getAnimatedValue())));
        return latLngAnimator;
    }

    public Animator CreateZoomAnimator(double currentZoom, double targetZoom, long duration) {
        ValueAnimator zoomAnimator = ValueAnimator.ofFloat((float) currentZoom, (float) targetZoom);
        zoomAnimator.setDuration(duration);
        zoomAnimator.setInterpolator(new FastOutSlowInInterpolator());
        zoomAnimator.addUpdateListener(animation -> mapbox_map.moveCamera(CameraUpdateFactory.zoomTo((Float) animation.getAnimatedValue())));
        return zoomAnimator;
    }

    public Animator CreateBearingAnimator(double currentBearing, double targetBearing, long duration) {
        ValueAnimator bearingAnimator = ValueAnimator.ofFloat((float) currentBearing, (float) targetBearing);
        bearingAnimator.setDuration(duration);
        bearingAnimator.setInterpolator(new FastOutSlowInInterpolator());
        bearingAnimator.addUpdateListener(animation -> mapbox_map.moveCamera(CameraUpdateFactory.bearingTo((Float) animation.getAnimatedValue())));
        return bearingAnimator;
    }

    public Animator CreateTiltAnimator(double currentTilt, double targetTilt, long duration) {
        ValueAnimator tiltAnimator = ValueAnimator.ofFloat((float) currentTilt, (float) targetTilt);
        tiltAnimator.setDuration(duration);
        tiltAnimator.setInterpolator(new FastOutSlowInInterpolator());
        tiltAnimator.addUpdateListener(animation -> mapbox_map.moveCamera(CameraUpdateFactory.tiltTo((Float) animation.getAnimatedValue())));
        return tiltAnimator;
    }

    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
        private final LatLng latLng = new LatLng();
        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }
}
