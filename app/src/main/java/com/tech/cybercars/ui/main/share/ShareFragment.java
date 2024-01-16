package com.tech.cybercars.ui.main.share;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentShareBinding;
import com.tech.cybercars.ui.base.BaseFragment;

public class ShareFragment extends BaseFragment<FragmentShareBinding, ShareViewModel>{

    @NonNull
    @Override
    protected ShareViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ShareViewModel.class);
    }

    @Override
    protected FragmentShareBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share, container, false);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }
}