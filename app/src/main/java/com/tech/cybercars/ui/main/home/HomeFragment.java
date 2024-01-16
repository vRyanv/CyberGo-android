package com.tech.cybercars.ui.main.home;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentHomeBinding;
import com.tech.cybercars.services.location.LocationService;
import com.tech.cybercars.services.mapbox.MapBoxService;
import com.tech.cybercars.ui.base.BaseFragment;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    MapBoxService mapbox_service;
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
        MapBoxService.MapBoxInstance(requireContext(), getString(R.string.mapbox_access_token));
    }

    @Override
    protected void InitCommon() {
//        binding.btnCarType.setOnClickListener(v-> {
//            binding.btnCarType.setBackgroundResource(R.drawable.shape_transport_type_selected);
//        });

        CarmenFeature home = CarmenFeature.builder().text("MY HOME")
                .geometry(Point.fromLngLat(105.78073579458483, 10.033432533756065))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-my0-home")
                .properties(new JsonObject())
                .build();
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

    private void InitMapBox() {
        binding.mapView.onCreate(savedInstanceState);
        mapbox_service = new MapBoxService(
                requireContext(),
                binding.mapView,
                Style.OUTDOORS,
                style -> {

                    String geojsonSourceLayerId = "geojsonSourceLayerId";
                    String symbolIconId = "symbolIconId";
//                    StartMapBoxService();
                    CarmenFeature home = CarmenFeature.builder().text("Mapbox SF Office")
                            .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                            .placeName("50 Beale St, San Francisco, CA")
                            .id("mapbox-sf")
                            .properties(new JsonObject())
                            .build();

                    CarmenFeature work = CarmenFeature.builder().text("Mapbox DC Office")
                            .placeName("740 15th Street NW, Washington DC")
                            .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                            .id("mapbox-dc")
                            .properties(new JsonObject())
                            .build();
                    style.addImage(symbolIconId,BitmapFactory.decodeResource(requireContext().getResources(), R.drawable.location_pin));
                    style.addSource(new GeoJsonSource(geojsonSourceLayerId));
                    style.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                            iconImage(symbolIconId),
                            iconOffset(new Float[] {0f, -8f})
                    ));

                    binding.btnToggleBottomSheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                          Intent intent =   new PlacePicker.IntentBuilder()
//
//                                    .accessToken(getString(R.string.mapbox_access_token))
//                                    .placeOptions(PlacePickerOptions.builder()
//                                            .statingCameraPosition(
//                                                    new CameraPosition.Builder()
//                                                    .target(new LatLng(40.7544, -73.9862))
//                                                            .zoom(16)
//                                                            .build()
//                                            )
//                                            .build()
//                                    )
//                                    .build(requireActivity());
//                            test.launch(intent);
                        }
                    });
//                    new Handler().postDelayed(() -> {
//                                Toast.makeText(requireContext(), "load", Toast.LENGTH_SHORT).show();
//                                binding.setIsLoading(false);
//                            },
//                            1000);

                    mapbox_service.SetOnCameraStartListener((reason)->{
                        if(reason == MapboxMap.OnCameraMoveStartedListener.REASON_API_GESTURE){
                            binding.btnFocusCurrentLocation.setImageResource(R.drawable.ic_not_focus_location);
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                }
        );

        binding.inputFromMap.getEditText().setOnClickListener(view->{
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show();
        });
    }

    private final ActivityResultLauncher<Intent> test = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getData() != null){
            Intent intent = result.getData();
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
        if (MapBoxService.CheckPermissionUserLocation(requireContext())) {
            if (LocationService.IsGPSActivated(requireContext())) {
                mapbox_service.ActivateLocationComponent();
                binding.btnFocusCurrentLocation.setImageResource(R.drawable.ic_focus_my_location);
            } else {
                OpenLocationSetting();
            }
        } else {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void StartTracking() {
        if (MapBoxService.CheckPermissionUserLocation(requireContext())) {
            if (LocationService.IsGPSActivated(requireContext())) {
                if (!mapbox_service.GetMapBoxMap().getLocationComponent().isLocationComponentActivated()) {
                    mapbox_service.ActivateLocationComponent();
                }
                mapbox_service.StartTrackingMode();
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
}