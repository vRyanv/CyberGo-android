package com.tech.cybercars.ui.main.fragment.go;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.cybercars.R;
import com.tech.cybercars.databinding.FragmentGoBinding;
import com.tech.cybercars.services.eventbus.ActionEvent;
import com.tech.cybercars.ui.main.fragment.go.find_trip.FindTransportActivity;

import org.greenrobot.eventbus.EventBus;

public class GoFragment extends Fragment {
    private FragmentGoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_go, container, false);


        InitView();

        return binding.getRoot();
    }

    private void InitView(){
        binding.cardFindTransport.setOnClickListener(view->{
            EventBus.getDefault().post(new ActionEvent(ActionEvent.NOTIFY));
//            startActivity(new Intent(requireContext(), FindTransportActivity.class));
        });

        binding.cardShareTransport.setOnClickListener(view->{
            startActivity(new Intent(requireContext(), SelectTransportActivity.class));
        });
    }
}