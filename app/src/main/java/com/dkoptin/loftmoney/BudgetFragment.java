package com.dkoptin.loftmoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dkoptin.loftmoney.cells.money.MoneyAdapter;
import com.dkoptin.loftmoney.cells.money.MoneyCellModel;
import com.dkoptin.loftmoney.remote.AuthResponse;
import com.dkoptin.loftmoney.remote.MoneyApi;
import com.dkoptin.loftmoney.remote.MoneyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public class BudgetFragment extends Fragment implements MoneyAdapterListener, ActionMode.Callback {

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode mActionMode;

    private MoneyAdapter moneyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.costsRecyclerView);
        moneyAdapter = new MoneyAdapter();
        moneyAdapter.setMoneyAdapterListener(this);

        recyclerView.setAdapter(moneyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generateExpenses();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        generateExpenses();
    }


    private void generateExpenses() {
        final List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Prefs.TOKEN,"");
        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token, "expense")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        moneyAdapter.sortArrayList();
                    }
                })
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        for(MoneyItem moneyItem : moneyItems) {
                            moneyCellModels.add(MoneyCellModel.getInstance(moneyItem));
                        }
                        moneyAdapter.setData(moneyCellModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "Error " + throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onItemClick(MoneyCellModel moneyCellModel, int position) {
        moneyAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(MoneyCellModel moneyCellModel, int position) {
        if (mActionMode == null) {
            Objects.requireNonNull(getActivity()).startActionMode(this);
        }
        moneyAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {
                            removeItems();
                            actionMode.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Prefs.TOKEN, "");
        List<Integer> selectedItems = moneyAdapter.getSelectedItemIds();
        for (Integer itemId : selectedItems) {
            Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().removeItem(String.valueOf(itemId.intValue()), token)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            generateExpenses();
                            moneyAdapter.clearSelections();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("TAG", "Error" + throwable.getLocalizedMessage());
                        }
                    });
            compositeDisposable.add(disposable);

        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        moneyAdapter.clearSelections();
    }
}
