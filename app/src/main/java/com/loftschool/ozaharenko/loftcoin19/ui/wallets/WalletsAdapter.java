package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.Wallet;
import com.loftschool.ozaharenko.loftcoin19.databinding.LiWalletBinding;
import com.loftschool.ozaharenko.loftcoin19.util.ImageLoader;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;
import com.loftschool.ozaharenko.loftcoin19.widget.CircleOutlineProvider;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

class WalletsAdapter extends ListAdapter<Wallet, WalletsAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private final LogoUrlFormatter urlFormatter;

    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    @Inject
    WalletsAdapter(PriceFormatter priceFormatter,
                   LogoUrlFormatter urlFormatter,
                   ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Wallet>() {
            @Override
            public boolean areItemsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem.id(), newItem.id());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem, newItem);
            }

            @Override
            public Object getChangePayload(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return newItem;
            }
        });
        this.priceFormatter = priceFormatter;
        this.urlFormatter = urlFormatter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LiWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Wallet wallet = getItem(position);
        holder.binding.symbol.setText(wallet.coin().symbol());
        bindPrice(holder, wallet);
        imageLoader.load(urlFormatter.format(wallet.coin().id())).into(holder.binding.logo);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            final Wallet wallet = (Wallet) payloads.get(0);
            bindPrice(holder, wallet);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    private void bindPrice(@NonNull ViewHolder holder, @NonNull Wallet wallet) {
        final Resources res = holder.itemView.getResources();
        holder.binding.balance1.setText(res.getString(
                R.string.wallet_balance,
                priceFormatter.formatWithoutCurrencySymbol(wallet.balance()),
                wallet.coin().symbol()
        ));
        holder.binding.balance2.setText(priceFormatter.format(wallet.coin().price() * wallet.balance()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final LiWalletBinding binding;

        ViewHolder(@NonNull LiWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.logo.setOutlineProvider(new CircleOutlineProvider());
            binding.logo.setClipToOutline(true);
        }
    }
}
