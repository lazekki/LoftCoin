package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.databinding.DialogCurrencyBinding;
import com.loftschool.ozaharenko.loftcoin19.widget.OnItemClick;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CoinsSheetDialog extends BottomSheetDialogFragment {

    private static final String KEY_MODE = "mode";

    private static final int MODE_FROM = 0;

    private static final int MODE_TO = 1;

    private final CompositeDisposable fmtDisposable = new CompositeDisposable();

    private final CompositeDisposable viewDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory vmFactory;

    @Inject
    CoinsSheetAdapter adapter;

    private DialogCurrencyBinding binding;

    private  ConverterViewModel viewModel;


    private OnItemClick onItemClick;

    private int mode = MODE_FROM;

    static void chooseFrom(@NonNull FragmentManager fm) {
        show(fm, MODE_FROM);
    }

    static void chooseTo(@NonNull FragmentManager fm) {
        show(fm, MODE_TO);
    }

    private static void show(@NonNull FragmentManager fm, int mode) {
        final CoinsSheetDialog dialog = new CoinsSheetDialog();
        final Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, mode);
        dialog.setArguments(arguments);
        dialog.show(fm, CoinsSheetDialog.class.getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerConverterComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .build()
                .inject(this);
        viewModel = new ViewModelProvider(requireParentFragment(), vmFactory).get(ConverterViewModel.class);
        fmtDisposable.add(viewModel.topCoins().subscribe(adapter::submitList));
        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
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
        binding.recycler.setAdapter(adapter);
        onItemClick = new OnItemClick(requireContext(), v -> {
            final RecyclerView.ViewHolder viewHolder = binding.recycler.findContainingViewHolder(v);
            if (viewHolder != null) {
                if (MODE_FROM == mode) {
                    viewModel.fromCoin(viewHolder.getAdapterPosition());
                } else {
                    viewModel.toCoin(viewHolder.getAdapterPosition());
                }
            }
            dismissAllowingStateLoss();
        });
        binding.recycler.addOnItemTouchListener(onItemClick);
    }

    @Override
    public void onDestroyView() {
        binding.recycler.removeOnItemTouchListener(onItemClick);
        binding.recycler.setAdapter(null);
        viewDisposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        fmtDisposable.clear();
        super.onDestroy();
    }

}
