package com.tech.cybercars.ui.main.fragment.go.add_share_trip_information;

import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tech.cybercars.R;
import com.tech.cybercars.adapter.app.AppFragmentAdapter;
import com.tech.cybercars.adapter.share_trip_information.ShareTripInformationAdapter;
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

    }

    @Override
    protected void InitCommon() {
        ShareTripInformationAdapter share_trip_information_fm_adapter = new ShareTripInformationAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.paperShareTripInformation.setAdapter(share_trip_information_fm_adapter);
        String tab_name[] = new String[]{
                getString(R.string.location),
                getString(R.string.my_information),
                getString(R.string.my_transport)};
        new TabLayoutMediator(binding.tabShareTripInfo, binding.paperShareTripInformation, (tab, position) -> {
            tab.setText(tab_name[position]);
        }).attach();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }
}