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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.loftschool.ozaharenko.loftcoin19.BaseComponent;
import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.data.CmcApi;
import com.loftschool.ozaharenko.loftcoin19.data.Listings;
import com.loftschool.ozaharenko.loftcoin19.databinding.FragmentRatesBinding;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.loftschool.ozaharenko.loftcoin19.R.array.currencies_array;

public class RatesFragment extends Fragment {

    //we cannot inject into private fields, so we change 'private' to '@Inject'
    @Inject RatesAdapter adapter; //this is exactly injection into object. component -> RatesAdapter()

    private NavController navController;

    private FragmentRatesBinding binding;

    private RatesViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RatesComponent component = DaggerRatesComponent.builder()
                .baseComponent(BaseComponent.get(requireContext()))
                .fragment(this)
                .build();
        component.inject(this); //example of injection
        navController = Navigation.findNavController(requireActivity(), R.id.main_host);
        // data(repository) (<- view model can write to the data)-> view model -> ui
        viewModel = new ViewModelProvider(this)
                .get(RatesViewModel.class);

        //LifecycleOwner - abstract object to work with lifecycle.

        if (viewModel.isInitialized().compareAndSet(false, true)) {
            component.inject(viewModel);
        }

        //provider - subscriber model implementation:
        viewModel.getCoins().observe(this, adapter::submitList);

        viewModel.refresh();
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
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.currency_selector:
                navController.navigate(R.id.action_currency_dialog);
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
        setHasOptionsMenu(true);
        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recycler.setHasFixedSize(true);
        binding.recycler.swapAdapter(adapter, false);

        //Instead of:
        //binding.refresher.setOnRefreshListener(this::refresh);
        //we will use:

        viewModel.isLoading().observe(getViewLifecycleOwner(), binding.refresher::setRefreshing);
/*
        Using Alt + Enter, it is possible to reduce code above:

        1) select '{' after -> and press Alt + Enter + 'replace with expression lambda'. Code will changed from

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.refresher.setRefreshing((isLoading));
        });

        to:

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> binding.refresher.setRefreshing((isLoading)));

        2) select 'setRefreshing' + Alt + Enter + replace 'lambda with method reference'. Code will changed from

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> binding.refresher.setRefreshing((isLoading)));

        to

        viewModel.isLoading().observe(getViewLifecycleOwner(), binding.refresher::setRefreshing);

        How do we know that this replacement is possible? Because these methods are methods with the same signature.
 */

 /*
        Next, instead of using

        refresh();

        we will use:

        (with replace lambda:

        from:

        binding.refresher.setOnRefreshListener(() -> {
           viewModel.refresh();
        });

        to:

        binding.refresher.setOnRefreshListener(() -> viewModel.refresh());

        and with replace 'lambda with method reference'):

        from:

        binding.refresher.setOnRefreshListener(() -> viewModel.refresh());

        to:
 */
        binding.refresher.setOnRefreshListener(viewModel::refresh);

        //it is highly recommended to use method reference as mch as possible.

    }

    @Override
    public void onDestroyView() {
        binding.recycler.swapAdapter(null, false);
        super.onDestroyView();
    }
}
