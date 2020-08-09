package com.dkoptin.loftmoney;


import com.dkoptin.loftmoney.cells.money.MoneyCellModel;

public interface MoneyAdapterListener {

    public void onItemClick(MoneyCellModel moneyCellModel, int position);

    public void onItemLongClick(MoneyCellModel moneyCellModel, int position);
}
