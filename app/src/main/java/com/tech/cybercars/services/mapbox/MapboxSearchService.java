package com.tech.cybercars.services.mapbox;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapboxSearchService {

}

//    private MapboxGeocoding.Builder mapbox_geocoding_builder;
//
//    public MapboxSearchService(String token) {
//        this.mapbox_geocoding_builder = MapboxGeocoding.builder()
//                .accessToken(token)
//                .autocomplete(true)
//                .limit(10);
//    }
//    public MapboxGeocoding.Builder GetMapboxGeocodingBuilder(){
//        return mapbox_geocoding_builder;
//    }
//
//    public void execute(Callback<GeocodingResponse> callback){
//        mapbox_geocoding_builder.build().enqueueCall(callback);
//    }
//
//    public MapboxSearchService SetQuery(String query){
//        mapbox_geocoding_builder.query(query);
//        return this;
//    }
//    public MapboxSearchService SetQuery(Point point){
//        mapbox_geocoding_builder.query(point);
//        return this;
//    }
