package com.loftschool.ozaharenko.loftcoin19.ui.wallets;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.Transaction;
import com.loftschool.ozaharenko.loftcoin19.databinding.LiTransactionBinding;
import com.loftschool.ozaharenko.loftcoin19.util.PriceFormatter;

import java.util.Objects;

import javax.inject.Inject;

class TransactionsAdapter extends ListAdapter<Transaction, TransactionsAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private LayoutInflater inflater;

    @Inject
    TransactionsAdapter(PriceFormatter priceFormatter) {
        super(new DiffUtil.ItemCallback<Transaction>() {
            @Override
            public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem.id(), newItem.id());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.priceFormatter = priceFormatter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LiTransactionBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = getItem(position);

        int amountFormat = R.string.negative_amount;
        int fiatAmountFormat = R.string.negative_change;
        int fiatAmountColor = R.color.colorNegative;
        if (transaction.amount() > 0) {
            amountFormat = R.string.positive_amount;
            fiatAmountFormat = R.string.positive_change;
            fiatAmountColor = R.color.colorPositive;
        }

        final Resources res = holder.itemView.getResources();

        holder.binding.amount1.setText(res.getString(
                amountFormat,
                priceFormatter.formatWithoutCurrencySymbol(transaction.amount()),
                transaction.coin().symbol()
        ));
        holder.binding.amount2.setText(res.getString(
                fiatAmountFormat,
                priceFormatter.format(transaction.amount() * transaction.coin().price())
        ));

        final Context context = holder.itemView.getContext();

        holder.binding.amount2.setTextColor(ContextCompat.getColor(context, fiatAmountColor));

        holder.binding.timestamp.setText(DateUtils.formatDateTime(context,
                transaction.timestamp().getTime(),
                DateUtils.FORMAT_SHOW_YEAR
        ));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final LiTransactionBinding binding;

        ViewHolder(@NonNull LiTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}