package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.databinding.LiRateBinding;
import com.loftschool.ozaharenko.loftcoin19.util.ChangeFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.ImageLoader;
import com.loftschool.ozaharenko.loftcoin19.util.LogoUrlFormatter;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;
import com.loftschool.ozaharenko.loftcoin19.widget.CircleOutlineProvider;

import java.util.Objects;

import javax.inject.Inject;

class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private final ChangeFormatter changeFormatter;

    private final LogoUrlFormatter logoFormatter;

    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    //@Inject - requires for the component, what we are injecting (see RatesFragment.java -> row 51
    //this word instructs Dagger that this is injectable class and it is allowed to create a factory for him
    //we inject something into constructor - there is injection
    @Inject RatesAdapter(PriceFormatter priceFormatter,
                         ChangeFormatter changeFormatter,
                         LogoUrlFormatter logo,
                         ImageLoader imageLoader) {
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
        this.priceFormatter = priceFormatter;
        this.changeFormatter = changeFormatter;
        this.logoFormatter = logo;
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
        return new ViewHolder(LiRateBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);
        holder.binding.symbol.setText(coin.symbol());

        holder.binding.price.setText(priceFormatter.format(coin.price()));
        holder.binding.change.setText(changeFormatter.format(coin.change24h()));

        final Context context = holder.itemView.getContext();
        if (coin.change24h() >= 0) {
            holder.binding.change.setTextColor(ContextCompat
                    .getColor(context, R.color.colorPositive));
        } else if (coin.change24h() < 0) {
            holder.binding.change.setTextColor(ContextCompat
                    .getColor(context, R.color.colorNegative));
        }

        imageLoader.load(logoFormatter.format(coin.id())).into(holder.binding.logo);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat
                    .getColor(context, R.color.dark_three));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat
                    .getColor(context, R.color.dark_two));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final LiRateBinding binding;

        ViewHolder(@NonNull LiRateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.logo.setOutlineProvider(new CircleOutlineProvider());
            binding.logo.setClipToOutline(true);
        }
    }
}
