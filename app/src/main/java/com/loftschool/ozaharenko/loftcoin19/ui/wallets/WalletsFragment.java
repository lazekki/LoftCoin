package com.loftschool.ozaharenko.loftcoin19.ui.wallets;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentWalletsBinding;
import com.loftschool.ozaharenko.loftcoin19.widget.OffsetDecoration;
import com.loftschool.ozaharenko.loftcoin19.widget.RxRecyclerView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class WalletsFragment extends Fragment {

    private final CompositeDisposable fmtDisposable = new CompositeDisposable();

    private final CompositeDisposable viewDisposable = new CompositeDisposable();

    @Inject
    ViewModelProvider.Factory vmFactory;

    @Inject
    WalletsAdapter walletsAdapter;

    @Inject
    TransactionsAdapter transactionsAdapter;

    private FragmentWalletsBinding binding;

    private SnapHelper snapHelper;

    private WalletsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerWalletsComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .build()
                .inject(this);
        viewModel = new ViewModelProvider(this, vmFactory).get(WalletsViewModel.class);
        fmtDisposable.add(viewModel.wallets().subscribe(walletsAdapter::submitList));
        fmtDisposable.add(viewModel.transactions().subscribe(transactionsAdapter::submitList));
        fmtDisposable.add(viewModel.transactions().subscribe(t -> Timber.d("%s", t)));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWalletsBinding.inflate(inflater, container, false);
        final DisplayMetrics dm = inflater.getContext().getResources().getDisplayMetrics();
        final int walletWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, dm);
        final int spaceBetweenWallets = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, dm);
        final int padding = (dm.widthPixels - walletWidth - spaceBetweenWallets) / 2;
        binding.wallets.setPadding(padding, 0, padding, 0);
        binding.wallets.setClipToPadding(false);
        snapHelper = new PagerSnapHelper();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        binding.wallets.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        binding.wallets.setHasFixedSize(true);
        binding.wallets.swapAdapter(walletsAdapter, false);
        binding.wallets.addItemDecoration(new OffsetDecoration(view.getContext(), 16));
        snapHelper.attachToRecyclerView(binding.wallets);
        viewDisposable.add(viewModel.wallets().subscribe(wallets -> {
            binding.wallets.setVisibility(View.VISIBLE);
            binding.walletCard.setVisibility(View.GONE);
        }));
        viewDisposable.add(RxRecyclerView.onSnap(binding.wallets, snapHelper)
                .map(binding.wallets::findContainingViewHolder)
                .map(RecyclerView.ViewHolder::getAdapterPosition)
                .subscribe(viewModel::selectWallet));
        binding.transactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.transactions.swapAdapter(transactionsAdapter, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.wallets, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add == item.getItemId()) {
            viewDisposable.add(viewModel.addNextWallet().subscribe(wallet -> {
                final String message = getString(R.string.wallet_created, wallet.coin().symbol());
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        binding.wallets.swapAdapter(null, false);
        binding.transactions.swapAdapter(null, false);
        viewDisposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        fmtDisposable.clear();
        super.onDestroy();
    }

}
