package com.tech.cybercars.ui.main.fragment.message;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentMessageBinding;
import com.tech.cybercars.ui.base.BaseFragment;

public class MessageFragment extends BaseFragment<FragmentMessageBinding, MessageViewModel> {

    @NonNull
    @Override
    protected MessageViewModel InitViewModel() {
        return new ViewModelProvider(this).get(MessageViewModel.class);
    }

    @Override
    protected FragmentMessageBinding InitBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
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