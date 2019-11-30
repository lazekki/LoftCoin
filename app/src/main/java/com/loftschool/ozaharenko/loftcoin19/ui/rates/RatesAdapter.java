package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.Coin;
import com.loftschool.ozaharenko.loftcoin19.databinding.LiRateBinding;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    //@Inject - requires for the component, what we are injecting (see RatesFragment.java -> row 51
    //this word instructs Dagger that this is injectable class and it is allowed to create a factory for him
    //we inject something into constructor - there is injection
    @Inject RatesAdapter() {
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
        holder.binding.price.setText(NumberFormat
                .getCurrencyInstance().format(coin.price()));
        holder.binding.change.setText(String
                .format(Locale.US, "%.2f%%", coin.change24h()));
        final Context context = holder.itemView.getContext();
        if (coin.change24h() >= 0) {
            holder.binding.change.setTextColor(ContextCompat.getColor(context, R.color.colorPositive));
        } else if (coin.change24h() < 0) {
            holder.binding.change.setTextColor(ContextCompat.getColor(context, R.color.colorNegative));
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
        }
    }
}
