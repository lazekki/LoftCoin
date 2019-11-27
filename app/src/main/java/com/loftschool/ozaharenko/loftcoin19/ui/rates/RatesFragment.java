package com.loftschool.ozaharenko.loftcoin19.ui.rates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.CmcApi;
import com.loftschool.ozaharenko.loftcoin19.data.CmcApiProvider;
import com.loftschool.ozaharenko.loftcoin19.data.Listings;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentRatesBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.loftschool.ozaharenko.loftcoin19.R.array.currencies_array;

public class RatesFragment extends Fragment {

    private CmcApi api;

    private RatesAdapter adapter;

    private FragmentRatesBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new CmcApiProvider().get();
        adapter = new RatesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRatesBinding.inflate(inflater, container, false);
        Timber.d("%s", this);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.converter, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.currency_selector:
                onCreateDialogForCurrenciesSelection(null).show();
                return true;
            case R.id.menu_sorting:
                //to do something for menu_sorting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Dialog onCreateDialogForCurrenciesSelection(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.currency_selection_dialog_title)
               .setItems(currencies_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recycler.setHasFixedSize(true);
        binding.recycler.swapAdapter(adapter, false);
        binding.refresher.setOnRefreshListener(this::refresh);
        refresh();
    }

    @Override
    public void onDestroyView() {
        binding.recycler.swapAdapter(null, false);
        super.onDestroyView();
    }

    private void refresh() {
        binding.refresher.setRefreshing(true);
        api.listings().enqueue(new Callback<Listings>() {
            @Override
            public void onResponse(Call<Listings> call, Response<Listings> response) {
                binding.refresher.setRefreshing(false);
                final Listings body = response.body();
                if (body != null) {
                    adapter.submitList(body.coins());
                }
            }

            @Override
            public void onFailure(Call<Listings> call, Throwable t) {
                binding.refresher.setRefreshing(false);
                Timber.d(t);
            }
        });
    }
}
