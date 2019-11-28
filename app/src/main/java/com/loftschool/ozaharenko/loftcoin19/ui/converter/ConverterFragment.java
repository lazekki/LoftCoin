package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentConverterBinding;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentRatesBinding;

import timber.log.Timber;

public class ConverterFragment extends Fragment {

    private FragmentConverterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConverterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
