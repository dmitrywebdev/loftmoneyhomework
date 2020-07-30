package com.dkoptin.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dkoptin.loftmoney.cells.money.MoneyAdapter;
import com.dkoptin.loftmoney.cells.money.MoneyCellModel;
import com.dkoptin.loftmoney.remote.MoneyItem;
import com.dkoptin.loftmoney.remote.MoneyResponse;
import com.dkoptin.loftmoney.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {

    RecyclerView recyclerView;
    MoneyAdapter moneyAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String name;
    private String price;

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

        recyclerView.setAdapter(moneyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        FloatingActionButton addCellExpenses = view.findViewById(R.id.addCellExpeneses);
        addCellExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_ADD_ITEM);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        generateExpenses();
    }

    public static BudgetFragment newInstance(BudgetFragmentTags tag) {
        BudgetFragment myFragment = new BudgetFragment();

        Bundle args = new Bundle();
        args.putSerializable("someTag", tag);
        myFragment.setArguments(args);

        return myFragment;
    }

    private void generateExpenses() {
        final List<MoneyCellModel> moneyCellModels = new ArrayList<>();

        Disposable disposable = ((LoftApp) (Objects.requireNonNull(getActivity()).getApplication())).getMoneyApi().getMoney("expense")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoneyResponse>() {
                    @Override
                    public void accept(MoneyResponse moneyResponse) throws Exception {
                        for (MoneyItem moneyItem : moneyResponse.getMoneyItemList()){
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
//        moneyCellModels.add(new MoneyCellModel(name, (price + " ₽"), R.color.expenseColor));
//        return moneyCellModels;
    }

    private List<MoneyCellModel> generateIncome() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel(name, (price + " ₽"), R.color.colorTemp));
        return moneyCellModels;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        name = data.getStringExtra("name");
        price = data.getStringExtra("price");
      /*  if (((BudgetFragmentTags) getArguments().getSerializable("someTag")) == BudgetFragmentTags.EXPENSES) {
            moneyAdapter.addData(generateExpenses());
        } else {
            moneyAdapter.addData(generateIncome());
        }*/
    }
}
