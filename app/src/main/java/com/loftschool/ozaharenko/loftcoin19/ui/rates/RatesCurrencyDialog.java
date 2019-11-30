package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.Currency;
import com.loftschool.ozaharenko.loftcoin19.data.CurrencyRepo;
import com.loftschool.ozaharenko.loftcoin19.databinding.DialogCurrencyBinding;
import com.loftschool.ozaharenko.loftcoin19.widget.CurrencyAdapter;
import com.loftschool.ozaharenko.loftcoin19.widget.OnItemClick;

import java.util.Arrays;

import javax.inject.Inject;

import timber.log.Timber;

public class RatesCurrencyDialog extends AppCompatDialogFragment {

    @Inject CurrencyAdapter adapter;

    @Inject CurrencyRepo currencies;

    private DialogCurrencyBinding binding;

    private OnItemClick onItemClick;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerRatesComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .fragment(this)
                .build()
                .inject(this);
        adapter.submitList(currencies.availableCurrencies());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogCurrencyBinding.inflate(requireActivity().getLayoutInflater());
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.currency_chooser)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recycler.setAdapter(adapter);
        onItemClick = new OnItemClick(requireContext(), view -> {
            RecyclerView.ViewHolder viewHolder = binding.recycler
                    .findContainingViewHolder(view);
            if (viewHolder != null) {
                currencies.setCurrency(adapter.getItem(viewHolder.getAdapterPosition()));
            }
            dismissAllowingStateLoss();
        });
        binding.recycler.addOnItemTouchListener(onItemClick);
    }

    @Override
    public void onDestroy() {
        Timber.d("DEFAULT CURRENCY: %s", currencies.getCurrency());
        binding.recycler.removeOnItemTouchListener(onItemClick);
        binding.recycler.setAdapter(null);
        super.onDestroy();
    }
}
