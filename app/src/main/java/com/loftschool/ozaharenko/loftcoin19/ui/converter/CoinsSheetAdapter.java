package com.loftschool.ozaharenko.loftcoin19.ui.converter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.databinding.LiCoinSheetBinding;
import com.loftschool.ozaharenko.loftcoin19.util.ImageLoader;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;
import com.loftschool.ozaharenko.loftcoin19.widget.CircleOutlineProvider;

import java.util.Objects;

import javax.inject.Inject;

class CoinsSheetAdapter extends ListAdapter<Coin, CoinsSheetAdapter.ViewHolder> {

    private final LogoUrlFormatter logoFormatter;

    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    @Inject
    CoinsSheetAdapter(LogoUrlFormatter logoFormatter, ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Coin>() {
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.logoFormatter = logoFormatter;
        this.imageLoader = imageLoader;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LiCoinSheetBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);
        holder.binding.name.setText(coin.name() + " | " + coin.symbol());
        imageLoader.load(logoFormatter.format(coin.id())).into(holder.binding.logo);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final LiCoinSheetBinding binding;

        ViewHolder(@NonNull LiCoinSheetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.logo.setOutlineProvider(new CircleOutlineProvider());
            binding.logo.setClipToOutline(true);
        }
    }

}