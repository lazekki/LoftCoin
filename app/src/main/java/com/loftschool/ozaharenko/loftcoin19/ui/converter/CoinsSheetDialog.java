package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.databinding.DialogCurrencyBinding;

import javax.inject.Inject;

public class CoinsSheetDialog extends BottomSheetDialogFragment {

    @Inject
    ViewModelProvider.Factory vmFactory;

    private DialogCurrencyBinding binding;

    private  ConverterViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerConverterComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .build()
                .inject(this);
        viewModel = new ViewModelProvider(requireParentFragment(), vmFactory).get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogCurrencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onDestroyView() {
        binding.recycler.setAdapter(null);
        super.onDestroyView();
    }

}
