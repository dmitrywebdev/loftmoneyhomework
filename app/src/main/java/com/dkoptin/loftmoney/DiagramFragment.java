package com.dkoptin.loftmoney;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dkoptin.loftmoney.remote.BalanceResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DiagramFragment extends Fragment {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView availableBalance;
    private TextView expenseValue;
    private TextView incomeValue;
    private BalanceView balanceView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diogram, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        availableBalance = view.findViewById(R.id.textBalanceAvailable);
        expenseValue = view.findViewById(R.id.expenseValue);
        incomeValue = view.findViewById(R.id.incomeValue);
        balanceView = view.findViewById(R.id.balanceView);
    }

    @Override
    public void onResume() {
        super.onResume();
        putTotalValues();
    }

    private void putTotalValues() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Prefs.TOKEN, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getBalance(token)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BalanceResponse>() {
                    @Override
                    public void accept(BalanceResponse balanceResponse) throws Exception {
                        final String totalExpenses = balanceResponse.getTotalExpenses();
                        final String totalIncomes = balanceResponse.getTotalIncomes();

                        expenseValue.setText(String.valueOf(totalExpenses));
                        incomeValue.setText(String.valueOf(totalIncomes));

                        int totalExpensesInt = Integer.parseInt(totalExpenses);
                        int totalIncomesInt = Integer.parseInt(totalIncomes);

                        availableBalance.setText(String.valueOf(totalIncomesInt - totalExpensesInt));
                        balanceView.update(totalExpensesInt,totalIncomesInt);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }

    public static DiagramFragment getInstance() {
        return new DiagramFragment();
    }
}
