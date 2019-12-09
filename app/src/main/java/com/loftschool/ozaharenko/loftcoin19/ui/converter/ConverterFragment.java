package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentConverterBinding;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentRatesBinding;

import javax.inject.Inject;

import timber.log.Timber;

public class ConverterFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory vmFactory;

    private FragmentConverterBinding binding;

    private ConverterViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerConverterComponent.builder()
            .baseComponent(BaseComponent.get(requireContext()))
            .build()
            .inject(this);
        viewModel = new ViewModelProvider(this, vmFactory).get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConverterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new CoinsSheetDialog().show(getChildFragmentManager(), CoinsSheetDialog.class.getName());
    }
}
