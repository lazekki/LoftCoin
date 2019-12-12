package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentConverterBinding;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ConverterFragment extends Fragment {

    private final CompositeDisposable fmtDisposable = new CompositeDisposable();

    private final CompositeDisposable viewDisposable = new CompositeDisposable();

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConverterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDisposable.add(viewModel.fromCoin().subscribe(coin -> {
            binding.fromCoin.setText(coin.symbol());
        }));
        viewDisposable.add(viewModel.toCoin().subscribe(coin -> {
            binding.toCoin.setText(coin.symbol());
        }));

        viewDisposable.add(RxTextView.textChanges(binding.from).subscribe(text -> {
            viewModel.fromValue(text.toString());
        }));
        viewDisposable.add(RxTextView.textChanges(binding.to).subscribe(text -> {
            viewModel.toValue(text.toString());
        }));

        viewDisposable.add(viewModel.fromValue()
                .filter(value -> !binding.from.hasFocus())
                .subscribe(binding.from::setText));

        viewDisposable.add(viewModel.toValue()
                .filter(value -> !binding.to.hasFocus())
                .subscribe(binding.to::setText));

        viewDisposable.add(RxView.clicks(binding.fromCoin).subscribe(u -> {
            CoinsSheetDialog.chooseFrom(getChildFragmentManager());
        }));
        viewDisposable.add(RxView.clicks(binding.toCoin).subscribe(u -> {
            CoinsSheetDialog.chooseTo(getChildFragmentManager());
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        fmtDisposable.clear();
        super.onDestroy();
    }
}
