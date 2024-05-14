package com.tech.cybercars.ui.main.fragment.go.share_trip.single_destination;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.tech.cybercars.R;
import com.tech.cybercars.constant.PickLocation;
import com.tech.cybercars.constant.StatusCode;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.data.models.Vehicle;
import com.tech.cybercars.data.remote.base.CallServerStatus;
import com.tech.cybercars.data.remote.map.reverse_geocoding.ReverseGeocodingResponse;
import com.tech.cybercars.data.repositories.MapRepository;
import com.tech.cybercars.services.mapbox.MapboxNavigationService;
import com.tech.cybercars.ui.base.BaseViewModel;
import com.tech.cybercars.utils.DateUtil;
import com.tech.cybercars.utils.Helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleShareTripViewModel extends BaseViewModel {
    public final MutableLiveData<Boolean> is_success_reverse_geocoding = new MutableLiveData<>();
    public final MutableLiveData<Vehicle> vehicle = new MutableLiveData<>();
    public final MutableLiveData<String> route_time = new MutableLiveData<>();
    public final MutableLiveData<String> route_distance = new MutableLiveData<>();
    public final MutableLiveData<String> origin_address = new MutableLiveData<>();
    public final MutableLiveData<String> destination_address = new MutableLiveData<>();
    public final MutableLiveData<String> error_get_route = new MutableLiveData<>();
    public final MutableLiveData<DirectionsRoute> current_route = new MutableLiveData<>();
    public final MutableLiveData<List<ReverseGeocodingResponse>> search_address_result = new MutableLiveData<>();
    public ReverseGeocodingResponse origin_reverse;
    public ReverseGeocodingResponse destination_reverse;
    private final MapRepository map_repository = MapRepository.GetInstance();
    private int pick_location;

    public void HandleSearchAddress(String address) {
        map_repository.SearchAddress(address, 10,
                search_address_callback::CallSuccess,
                search_address_callback::CallFail
        );
    }

    private final CallServerStatus<List<ReverseGeocodingResponse>> search_address_callback = new CallServerStatus<List<ReverseGeocodingResponse>>() {
        @Override
        public void CallSuccess(Response<List<ReverseGeocodingResponse>> response) {
            if (response.isSuccessful()) {
                search_address_result.postValue(response.body());
            } else {
                search_address_result.postValue(null);
            }
        }

        @Override
        public void CallFail(Throwable error) {
            search_address_result.postValue(null);
        }
    };

    public void HandleGetRoute(String directions_criteria_profile) {
        Point origin = Point.fromLngLat(origin_reverse.lng, origin_reverse.lat);
        Point destination = Point.fromLngLat(destination_reverse.lng, destination_reverse.lat);
        new MapboxNavigationService().GetRoute(
                origin,
                destination,
                directions_criteria_profile,
                getApplication().getString(R.string.mapbox_access_token),
                callback_direction_callback
        );
    }

    private final Callback<DirectionsResponse> callback_direction_callback = new Callback<DirectionsResponse>() {
        @Override
        public void onResponse(@NonNull Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
            if (response.body() == null) {
                error_get_route.postValue(getApplication().getString(R.string.your_request_is_invalid));
                return;
            } else if (response.body().routes().size() < 1) {
                error_get_route.postValue(getApplication().getString(R.string.no_suitable_route_was_found));
                return;
            }
            DirectionsRoute route = response.body().routes().get(0);
            current_route.postValue(route);
            String time = DateUtil.ConvertSecondToHour(route.duration());
            route_time.postValue(time);

            Double distance_meter = route.distance();
            if (distance_meter < 1000) {
                route_distance.postValue(Math.round(distance_meter) + "m");
            } else {
                String rounded_distance = Helper.ConvertMeterToKiloMeterString(distance_meter);
                route_distance.postValue(rounded_distance + " Km");
            }

        }

        @Override
        public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable t) {
            is_loading.postValue(false);
            error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        }
    };

    public void HandleFindAddress(LatLng lat_lng, int pick_location) {
        is_loading.setValue(true);
        map_repository.FindAddress(
                lat_lng,
                response -> {
                    this.pick_location = pick_location;
                    reverse_geocoding_callback.CallSuccess(response);
                },
                reverse_geocoding_callback::CallFail
        );
    }

    private final CallServerStatus<ReverseGeocodingResponse> reverse_geocoding_callback = new CallServerStatus<ReverseGeocodingResponse>() {
        @Override
        public void CallSuccess(Response<ReverseGeocodingResponse> response) {
            is_loading.postValue(false);
            assert response.body() != null;
            if (response.body().code == StatusCode.OK) {
                if (pick_location == PickLocation.PICK_START_POINT) {
                    origin_address.postValue(response.body().display_name);
                    origin_reverse = response.body();
                } else {
                    destination_address.postValue(response.body().display_name);
                    destination_reverse = response.body();
                }
                is_success_reverse_geocoding.postValue(true);
            } else if (response.body().code == StatusCode.NOT_FOUND) {
                is_success_reverse_geocoding.postValue(false);
            } else if (response.body().code == StatusCode.BAD_REQUEST) {
                error_call_server.postValue(getApplication().getString(R.string.your_request_is_invalid));
            }
        }

        @Override
        public void CallFail(Throwable error) {
            is_loading.postValue(false);
            Log.e(Tag.CYBER_DEBUG, "reverse_geocoding_callback: " + error.getMessage());
            error_call_server.postValue(getApplication().getString(R.string.can_not_connect_to_server));
        }
    };

    public SingleShareTripViewModel(@NonNull Application application) {
        super(application);
    }

}
