package com.tech.cybercars.ui.main.fragment.home;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentHomeBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapboxMapService;
import com.tech.cybercars.ui.base.BaseFragment;

import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    MapboxMapService mapbox_service;
    private BottomSheetBehavior<ConstraintLayout> bottom_sheet_behavior;

    @NonNull
    @Override
    protected HomeViewModel InitViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected FragmentHomeBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setViewModel(this.view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {
        MapboxMapService.MapBoxInstance(requireContext(), getString(R.string.mapbox_access_token));
    }

    @Override
    protected void InitCommon() {
//        binding.btnCarType.setOnClickListener(v-> {
//            binding.btnCarType.setBackgroundResource(R.drawable.shape_transport_type_selected);
//        });
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void InitView() {
        InitMapBox();
        InitBottomSheet();

        binding.btnFocusCurrentLocation.setOnClickListener(view -> {
            StartTracking();
        });

        binding.btnToggleBottomSheet.setOnClickListener(view -> {
            if (bottom_sheet_behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                this.bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                this.bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void InitBottomSheet() {
        this.bottom_sheet_behavior = BottomSheetBehavior.from(binding.bsLocationPicker);
        float min_height = getResources().getDimension(com.intuit.sdp.R.dimen._25sdp);
        this.bottom_sheet_behavior.setPeekHeight(Math.round(min_height));
        float max_height = getResources().getDimension(com.intuit.sdp.R.dimen._200sdp);
        bottom_sheet_behavior.setMaxHeight(Math.round(max_height));
        this.bottom_sheet_behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    icon_arrow_collap.setRotation(-90);
//                } else {
//                    icon_arrow_collap.setRotation(90);
//                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void InitMapBox() {
        binding.mapView.onCreate(savedInstanceState);
        mapbox_service = new MapboxMapService(
                requireContext(),
                binding.mapView,
                Style.OUTDOORS,
                style -> {
                    StartMapBoxService();
                    binding.btnToggleBottomSheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        }
                    });

                    mapbox_service.SetOnCameraStartListener((reason)->{
                        if(reason == MapboxMap.OnCameraMoveStartedListener.REASON_API_GESTURE){
                            binding.btnFocusCurrentLocation.setImageResource(R.drawable.ic_not_focus_location);
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                    AddLayer(style);
                }
        );

        binding.inputFromMap.getEditText().setOnClickListener(view->{
            ChooseStartLocation();
        });

        binding.inputToMap.getEditText().setOnClickListener(view->{
            ChooseEndLocation();
        });
    }
    private void ChooseEndLocation(){
        Intent intent =  new PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlacePickerOptions.builder()
                        .statingCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(40.7544, -73.9862))
                                        .zoom(16)
                                        .build()
                        )
                        .build()
                )
                .build(requireActivity());

        result_end_location.launch(intent);
    }
    private void ChooseStartLocation(){
        Intent intent =   new PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(PlacePickerOptions.builder()
                        .statingCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(40.7544, -73.9862))
                                        .zoom(16)
                                        .build()
                        )
                        .build()
                )
                .build(requireActivity());

        result_start_location.launch(intent);
    }
    private void AddLayer(Style loadedMapStyle){
        loadedMapStyle.addImage(symbolIconId, BitmapFactory.decodeResource(
                requireContext().getResources(), R.drawable.location_pin));
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private final ActivityResultLauncher<Intent>  result_start_location = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getData() != null){
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(result.getData());
            Point point = (Point) selectedCarmenFeature.geometry();
                binding.inputFromMap.getEditText().setText(selectedCarmenFeature.placeName());
                Style style = mapbox_service.GetMapBoxMap().getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    mapbox_service.GetMapBoxMap().animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }

    });

    private final ActivityResultLauncher<Intent>  result_end_location = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getData() != null){
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(result.getData());
            Point point = (Point) selectedCarmenFeature.geometry();
            binding.inputToMap.getEditText().setText(selectedCarmenFeature.placeName());
            Log.e("khang", selectedCarmenFeature.toJson());
            List<CarmenContext> contextList = selectedCarmenFeature.context();
            Style style = mapbox_service.GetMapBoxMap().getStyle();
            if (style != null) {
                GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                if (source != null) {
                    source.setGeoJson(FeatureCollection.fromFeatures(
                            new Feature[] {
                                    Feature.fromGeometry(selectedCarmenFeature.geometry())
                            })
                    );
                }

                mapbox_service.GetMapBoxMap().animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                        ((Point) selectedCarmenFeature.geometry()).longitude()))
                                .zoom(14)
                                .build()), 4000);
            }
        }

    });

    @Override
    protected void InitObserve() {

    }

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            StartMapBoxService();
        }
    });

    private final ActivityResultLauncher<Intent> open_GPS_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (LocationService.IsGPSActivated(requireContext())) {
            Toast.makeText(requireContext(), "Location is enabled", Toast.LENGTH_SHORT).show();
            StartTracking();
        } else {
            Toast.makeText(requireContext(), "Location setting change canceled", Toast.LENGTH_SHORT).show();
        }
    });

    private void StartMapBoxService() {
        if (MapboxMapService.CheckPermissionUserLocation(requireContext())) {
            if (LocationService.IsGPSActivated(requireContext())) {
                mapbox_service.ActivateLocationComponent(null);
                binding.btnFocusCurrentLocation.setImageResource(R.drawable.ic_focus_my_location);
            } else {
                OpenLocationSetting();
            }
        } else {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void StartTracking() {
        if (MapboxMapService.CheckPermissionUserLocation(requireContext())) {
            if (LocationService.IsGPSActivated(requireContext())) {
                if (!mapbox_service.GetMapBoxMap().getLocationComponent().isLocationComponentActivated()) {
                    mapbox_service.ActivateLocationComponent(null);
                }
                mapbox_service.StartTrackingMode(null);
                binding.btnFocusCurrentLocation.setImageResource(R.drawable.ic_focus_my_location);
            } else {
                OpenLocationSetting();
            }
        } else {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void OpenLocationSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        open_GPS_launcher.launch(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    private void bottom_dialog(){
        //        final Dialog dialog = new Dialog(requireContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_bottom_sheet);
//
//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
//        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
//
//        videoLayout.setOnClickListener(v -> {
//
//            dialog.dismiss();
//            Toast.makeText(requireContext(),"Upload a Video is clicked",Toast.LENGTH_SHORT).show();
//
//        });
//
//        shortsLayout.setOnClickListener(v -> {
//
//            dialog.dismiss();
//            Toast.makeText(requireContext(),"Create a short is Clicked",Toast.LENGTH_SHORT).show();
//
//        });
//
//        liveLayout.setOnClickListener(v -> {
//
//            dialog.dismiss();
//            Toast.makeText(requireContext(),"Go live is Clicked",Toast.LENGTH_SHORT).show();
//
//        });
//
//        cancelButton.setOnClickListener(view -> dialog.dismiss());
//
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}