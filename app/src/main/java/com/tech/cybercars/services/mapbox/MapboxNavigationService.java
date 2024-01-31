package com.tech.cybercars.services.mapbox;

import static com.mapbox.api.directions.v5.DirectionsCriteria.GEOMETRY_POLYLINE;

import android.os.Handler;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class MapboxNavigationService {
    public void GetRoute(Point origin, Point destination, String directions_criteria_profile, String token, Callback<DirectionsResponse> callback) {
        MapboxDirections map_direction_request = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .geometries(GEOMETRY_POLYLINE)
                .profile(directions_criteria_profile)
                .alternatives(true)
                .steps(true)
                .accessToken(token)
                .build();
        map_direction_request.enqueueCall(callback);
    }

    public static class DrawSnackRouteRunnable implements Runnable {
        private final MapboxMap mapbox_map;
        private final List<LegStep> steps;
        private final List<Feature> route_poly_line_feature_list;
        private final Handler handler;
        private int counter_index;
        private final String ROUTE_POLYLINE_SOURCE_ID;
        private final int DRAW_SPEED_MILLISECONDS;

        public DrawSnackRouteRunnable(MapboxMap mapboxMap, String ROUTE_POLYLINE_SOURCE_ID, int DRAW_SPEED_MILLISECONDS, List<LegStep> steps, Handler handler) {
            this.mapbox_map = mapboxMap;
            this.steps = steps;
            this.handler = handler;
            this.counter_index = 0;
            this.ROUTE_POLYLINE_SOURCE_ID = ROUTE_POLYLINE_SOURCE_ID;
            this.DRAW_SPEED_MILLISECONDS = DRAW_SPEED_MILLISECONDS;
            route_poly_line_feature_list = new ArrayList<>();
        }

        @Override
        public void run() {
            if (counter_index < steps.size()) {
                LegStep singleStep = steps.get(counter_index);
                if (singleStep != null && singleStep.geometry() != null) {
                    LineString lineStringRepresentingSingleStep = LineString.fromPolyline(
                            singleStep.geometry(), Constants.PRECISION_5);
                    Feature featureLineString = Feature.fromGeometry(lineStringRepresentingSingleStep);
                    route_poly_line_feature_list.add(featureLineString);
                }
                if (mapbox_map.getStyle() != null) {
                    GeoJsonSource source = mapbox_map.getStyle().getSourceAs(ROUTE_POLYLINE_SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(route_poly_line_feature_list));
                    }
                }
                counter_index++;
                handler.postDelayed(this, DRAW_SPEED_MILLISECONDS);
            }
        }
    }
}
