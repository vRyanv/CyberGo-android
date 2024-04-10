package com.tech.cybercars.ui.main.fragment.go.share_trip.add_share_trip_information;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.share_trip_information.ShareTripInformationAdapter;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.data.sub_models.TripSharing;
import com.tech.cybercars.databinding.ActivityAddShareTripInformationBinding;
import com.tech.cybercars.ui.base.BaseActivity;

public class AddShareTripInformationActivity extends BaseActivity<ActivityAddShareTripInformationBinding, AddShareTripInformationViewModel> {

    @NonNull
    @Override
    protected AddShareTripInformationViewModel InitViewModel() {
        return new ViewModelProvider(this).get(AddShareTripInformationViewModel.class);
    }

    @Override
    protected ActivityAddShareTripInformationBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_share_trip_information);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        binding.headerShareTripInformation.btnOutScreen.setOnClickListener(view -> {
            OnBackPress();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.current_page.observe(this, current_page ->{
            binding.paperShareTripInformation.setCurrentItem(current_page);
        });
    }

    @Override
    protected void InitCommon() {
        binding.paperShareTripInformation.setUserInputEnabled(false);
        ShareTripInformationAdapter share_trip_information_fm_adapter = new ShareTripInformationAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperShareTripInformation.setAdapter(share_trip_information_fm_adapter);
        String[] tab_name = new String[]{getString(R.string.location),getString(R.string.trip_information)};
        new TabLayoutMediator(binding.tabShareTripInfo, binding.paperShareTripInformation, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();

        TripSharing trip_sharing = (TripSharing) getIntent().getSerializableExtra(FieldName.TRIP_SHARING);
        view_model.trip_sharing.postValue(trip_sharing);

        String destination_type = getIntent().getStringExtra(FieldName.DESTINATION_TYPE);
        view_model.destination_type.setValue(destination_type);
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}