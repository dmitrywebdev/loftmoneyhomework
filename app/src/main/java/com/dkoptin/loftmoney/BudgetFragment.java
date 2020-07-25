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
import com.dkoptin.loftmoney.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    RecyclerView recyclerView;
    MoneyAdapter moneyAdapter;
    private String name;
    private String price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        name = data.getStringExtra("name");
        price = data.getStringExtra("price");
        moneyAdapter.addData(generateExpenses());
    }
    private List<MoneyCellModel> generateExpenses() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel(name, (price + " ₽"), R.color.expenseColor));
        return moneyCellModels;
    }
    private List<MoneyCellModel> generateIncome() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel(name, (price + " ₽"), R.color.colorTemp));
        return moneyCellModels;
    }
}
