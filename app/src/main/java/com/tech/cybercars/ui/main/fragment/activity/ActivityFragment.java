package com.tech.cybercars.ui.main.fragment.activity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentShareBinding;
import com.tech.cybercars.ui.base.BaseFragment;

public class ActivityFragment extends BaseFragment<FragmentShareBinding, ActivityViewModel>{

    @NonNull
    @Override
    protected ActivityViewModel InitViewModel() {
        return new ViewModelProvider(this).get(ActivityViewModel.class);
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