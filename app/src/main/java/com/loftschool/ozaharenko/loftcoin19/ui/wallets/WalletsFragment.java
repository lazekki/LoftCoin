package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentWalletsBinding;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class WalletsFragment extends Fragment {

    private final CompositeDisposable fmtDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory vmFactory;

    private WalletsFragment binding;

    private WalletsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWalletsComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .build()
                .inject(this);
        viewModel = new ViewModelProvider(this, vmFactory).get(WalletsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        Timber.d("%s", this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wallets, menu);
    }
}
